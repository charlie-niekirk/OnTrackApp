#!/usr/bin/env python3
"""Synchronize missing Compose Multiplatform string translations using GitHub Models."""

from __future__ import annotations

import argparse
import fnmatch
import json
import os
import re
import sys
from collections import Counter
from dataclasses import dataclass
from pathlib import Path
from typing import Any
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

import xml.etree.ElementTree as ET

try:
    import yaml
except ImportError:  # pragma: no cover - optional dependency
    yaml = None

PLACEHOLDER_PATTERN = re.compile(r"%(?:\d+\$)?[sd]")

LOCALE_DISPLAY_NAMES = {
    "de": "German",
    "es": "Spanish",
    "fr": "French",
    "it": "Italian",
}


class SyncError(RuntimeError):
    """Domain-specific error raised when synchronization fails."""


@dataclass(frozen=True)
class ProviderConfig:
    endpoint: str
    model: str
    temperature: float
    request_timeout_seconds: int


@dataclass(frozen=True)
class PullRequestConfig:
    branch: str
    title: str
    labels: list[str]


@dataclass(frozen=True)
class Config:
    source_globs: list[str]
    exclude_globs: list[str]
    target_locales: list[str]
    provider: ProviderConfig
    pr: PullRequestConfig


@dataclass(frozen=True)
class StringEntry:
    name: str
    text: str


@dataclass
class FileLocaleResult:
    source_file: str
    locale_file: str
    locale: str
    missing_keys: list[str]
    added_keys: int


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Sync missing translations for composeResources strings.xml files."
    )
    parser.add_argument(
        "--mode",
        required=True,
        choices=["check", "apply"],
        help="check: detect missing keys only; apply: generate and write missing translations",
    )
    parser.add_argument(
        "--config",
        default=".github/i18n/config.yml",
        help="Path to i18n config YAML file",
    )
    parser.add_argument(
        "--report-json",
        help="Optional path to write JSON report",
    )
    parser.add_argument(
        "--report-md",
        help="Optional path to write Markdown report",
    )
    return parser.parse_args()


def load_config(config_path: Path) -> Config:
    if not config_path.exists():
        raise SyncError(f"Config file not found: {config_path}")

    data = _load_config_data(config_path)

    source_globs = _required_str_list(data, "source_globs")
    exclude_globs = _optional_str_list(data, "exclude_globs")
    target_locales = _required_str_list(data, "target_locales")

    provider_raw = data.get("provider")
    if not isinstance(provider_raw, dict):
        raise SyncError("Config key 'provider' must be an object")

    endpoint = _required_str(provider_raw, "endpoint", parent="provider")
    model = _required_str(provider_raw, "model", parent="provider")
    temperature = provider_raw.get("temperature", 0)
    if not isinstance(temperature, (int, float)):
        raise SyncError("Config key 'provider.temperature' must be a number")

    timeout = provider_raw.get("request_timeout_seconds", 90)
    if not isinstance(timeout, int) or timeout <= 0:
        raise SyncError("Config key 'provider.request_timeout_seconds' must be a positive integer")

    pr_raw = data.get("pr")
    if not isinstance(pr_raw, dict):
        raise SyncError("Config key 'pr' must be an object")

    pr_branch = _required_str(pr_raw, "branch", parent="pr")
    pr_title = _required_str(pr_raw, "title", parent="pr")
    pr_labels = _optional_str_list(pr_raw, "labels")

    return Config(
        source_globs=source_globs,
        exclude_globs=exclude_globs,
        target_locales=target_locales,
        provider=ProviderConfig(
            endpoint=endpoint,
            model=model,
            temperature=float(temperature),
            request_timeout_seconds=timeout,
        ),
        pr=PullRequestConfig(branch=pr_branch, title=pr_title, labels=pr_labels),
    )


def _load_config_data(config_path: Path) -> dict[str, Any]:
    raw = config_path.read_text(encoding="utf-8")

    try:
        data = json.loads(raw)
    except json.JSONDecodeError:
        if yaml is None:
            raise SyncError(
                "Config is not valid JSON and PyYAML is not installed. "
                "Either install PyYAML or use JSON-formatted config."
            )
        try:
            data = yaml.safe_load(raw) or {}
        except yaml.YAMLError as exc:
            raise SyncError(f"Failed to parse config file {config_path}: {exc}") from exc

    if not isinstance(data, dict):
        raise SyncError(f"Config root must be an object: {config_path}")

    return data


def _required_str(data: dict[str, Any], key: str, parent: str | None = None) -> str:
    value = data.get(key)
    if not isinstance(value, str) or not value.strip():
        prefix = f"{parent}." if parent else ""
        raise SyncError(f"Config key '{prefix}{key}' must be a non-empty string")
    return value


def _required_str_list(data: dict[str, Any], key: str) -> list[str]:
    values = _optional_str_list(data, key)
    if not values:
        raise SyncError(f"Config key '{key}' must contain at least one string value")
    return values


def _optional_str_list(data: dict[str, Any], key: str) -> list[str]:
    value = data.get(key, [])
    if value is None:
        return []
    if not isinstance(value, list) or not all(isinstance(item, str) and item for item in value):
        raise SyncError(f"Config key '{key}' must be a list of non-empty strings")
    return list(value)


def discover_source_files(repo_root: Path, config: Config) -> list[Path]:
    discovered: set[Path] = set()

    for pattern in config.source_globs:
        for candidate in repo_root.glob(pattern):
            if not candidate.is_file():
                continue
            rel_path = candidate.relative_to(repo_root).as_posix()
            if _is_excluded(rel_path, config.exclude_globs):
                continue
            discovered.add(candidate.resolve())

    return sorted(discovered, key=lambda path: path.relative_to(repo_root).as_posix())


def _is_excluded(path: str, exclude_globs: list[str]) -> bool:
    return any(fnmatch.fnmatch(path, pattern) for pattern in exclude_globs)


def parse_string_entries(xml_path: Path) -> tuple[ET.ElementTree, ET.Element, list[StringEntry], dict[str, ET.Element]]:
    try:
        tree = ET.parse(xml_path)
    except ET.ParseError as exc:
        raise SyncError(f"Malformed XML in {xml_path}: {exc}") from exc

    root = tree.getroot()
    if root.tag != "resources":
        raise SyncError(f"Invalid root tag in {xml_path}: expected <resources>, found <{root.tag}>")

    entries: list[StringEntry] = []
    element_by_name: dict[str, ET.Element] = {}

    for node in list(root):
        if node.tag != "string":
            continue

        name = node.attrib.get("name", "").strip()
        if not name:
            raise SyncError(f"String entry without a name attribute in {xml_path}")

        if name in element_by_name:
            raise SyncError(f"Duplicate string key '{name}' in {xml_path}")

        text_value = node.text or ""
        entries.append(StringEntry(name=name, text=text_value))
        element_by_name[name] = node

    return tree, root, entries, element_by_name


def initialize_locale_tree(locale_path: Path) -> tuple[ET.ElementTree, ET.Element, list[StringEntry], dict[str, ET.Element]]:
    if locale_path.exists():
        return parse_string_entries(locale_path)

    root = ET.Element("resources")
    tree = ET.ElementTree(root)
    return tree, root, [], {}


def locale_path_for(source_path: Path, locale: str) -> Path:
    values_dir = source_path.parent
    if values_dir.name != "values":
        raise SyncError(f"Expected source strings.xml to be in a values directory: {source_path}")
    return values_dir.with_name(f"values-{locale}") / source_path.name


def placeholder_counter(text: str) -> Counter[str]:
    return Counter(PLACEHOLDER_PATTERN.findall(text))


def generate_translations(
    provider: ProviderConfig,
    token: str,
    source_file: Path,
    locale: str,
    entries: list[StringEntry],
) -> dict[str, str]:
    if not entries:
        return {}

    locale_name = LOCALE_DISPLAY_NAMES.get(locale, locale)

    expected = {entry.name: entry.text for entry in entries}
    expected_json = json.dumps(expected, ensure_ascii=False, indent=2)

    system_prompt = (
        "You are a software localization translator for Android/Kotlin Compose strings. "
        "Return only valid JSON. Do not include markdown, code fences, or extra text."
    )

    feedback: str | None = None
    last_error: SyncError | None = None

    for attempt in range(2):
        user_prompt = _build_user_prompt(
            locale=locale,
            locale_name=locale_name,
            expected_json=expected_json,
            source_file=source_file,
            feedback=feedback,
        )

        response = call_models_api(
            provider=provider,
            token=token,
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_prompt},
            ],
        )

        try:
            payload = parse_model_content(response)
            return validate_translation_payload(entries, payload)
        except SyncError as exc:
            last_error = exc
            feedback = str(exc)

    assert last_error is not None
    raise SyncError(
        f"Model response validation failed for {source_file} locale '{locale}' after retry: {last_error}"
    )


def _build_user_prompt(
    locale: str,
    locale_name: str,
    expected_json: str,
    source_file: Path,
    feedback: str | None,
) -> str:
    base_prompt = (
        f"Translate each value in the provided JSON object into {locale_name} ({locale}).\n"
        "Rules:\n"
        "1) Return a single flat JSON object where keys are identical to the input keys.\n"
        "2) Preserve placeholders exactly (for example: %1$s, %1$d).\n"
        "3) Preserve intent and tone for a transit app UI.\n"
        "4) Do not change proper names unless natural in target language.\n"
        "5) Keep punctuation reasonable and avoid surrounding quotes.\n"
        "6) If text is already language-neutral, return it unchanged.\n\n"
        f"Source file context: {source_file.as_posix()}\n"
        "Input JSON:\n"
        f"{expected_json}"
    )

    if not feedback:
        return base_prompt

    return (
        f"Previous response was invalid: {feedback}\n"
        "Regenerate the full response and follow the rules strictly.\n\n"
        f"{base_prompt}"
    )


def call_models_api(provider: ProviderConfig, token: str, messages: list[dict[str, str]]) -> dict[str, Any]:
    body = {
        "model": provider.model,
        "temperature": provider.temperature,
        "messages": messages,
    }

    request = Request(
        url=provider.endpoint,
        data=json.dumps(body).encode("utf-8"),
        method="POST",
        headers={
            "Authorization": f"Bearer {token}",
            "Content-Type": "application/json",
            "Accept": "application/json",
            "X-GitHub-Api-Version": "2022-11-28",
            "User-Agent": "ontrackapp-i18n-sync",
        },
    )

    try:
        with urlopen(request, timeout=provider.request_timeout_seconds) as response:
            return json.loads(response.read().decode("utf-8"))
    except HTTPError as exc:
        error_body = exc.read().decode("utf-8", errors="replace")
        raise SyncError(f"GitHub Models API error {exc.code}: {error_body}") from exc
    except URLError as exc:
        raise SyncError(f"Failed to reach GitHub Models API: {exc}") from exc
    except json.JSONDecodeError as exc:
        raise SyncError(f"GitHub Models API returned non-JSON response: {exc}") from exc


def parse_model_content(response: dict[str, Any]) -> dict[str, Any]:
    choices = response.get("choices")
    if not isinstance(choices, list) or not choices:
        raise SyncError("Model response does not contain any choices")

    first_choice = choices[0]
    if not isinstance(first_choice, dict):
        raise SyncError("Model choice payload is invalid")

    message = first_choice.get("message")
    if not isinstance(message, dict):
        raise SyncError("Model response choice does not contain a message")

    content = message.get("content")

    text_content = ""
    if isinstance(content, str):
        text_content = content
    elif isinstance(content, list):
        parts: list[str] = []
        for item in content:
            if isinstance(item, dict):
                text = item.get("text")
                if isinstance(text, str):
                    parts.append(text)
        text_content = "".join(parts)
    else:
        raise SyncError("Model response content is missing")

    text_content = text_content.strip()
    if not text_content:
        raise SyncError("Model response content is empty")

    parsed = _parse_json_object_from_text(text_content)
    if not isinstance(parsed, dict):
        raise SyncError("Model response is not a JSON object")

    return parsed


def _parse_json_object_from_text(content: str) -> Any:
    try:
        return json.loads(content)
    except json.JSONDecodeError:
        pass

    fence_match = re.search(r"```(?:json)?\s*(\{.*\})\s*```", content, flags=re.DOTALL)
    if fence_match:
        fenced = fence_match.group(1)
        try:
            return json.loads(fenced)
        except json.JSONDecodeError:
            pass

    snippet = _extract_first_json_object(content)
    if snippet is None:
        raise SyncError("Model response did not include parseable JSON")

    try:
        return json.loads(snippet)
    except json.JSONDecodeError as exc:
        raise SyncError(f"Failed to parse JSON object from model response: {exc}") from exc


def _extract_first_json_object(text: str) -> str | None:
    start = text.find("{")
    if start == -1:
        return None

    depth = 0
    in_string = False
    escaped = False

    for index in range(start, len(text)):
        char = text[index]

        if in_string:
            if escaped:
                escaped = False
            elif char == "\\":
                escaped = True
            elif char == '"':
                in_string = False
            continue

        if char == '"':
            in_string = True
            continue

        if char == "{":
            depth += 1
        elif char == "}":
            depth -= 1
            if depth == 0:
                return text[start : index + 1]

    return None


def validate_translation_payload(entries: list[StringEntry], payload: dict[str, Any]) -> dict[str, str]:
    expected_keys = [entry.name for entry in entries]
    payload_keys = list(payload.keys())

    missing_in_payload = [key for key in expected_keys if key not in payload]
    extra_in_payload = [key for key in payload_keys if key not in expected_keys]

    if missing_in_payload or extra_in_payload:
        raise SyncError(
            "Translation key mismatch: "
            f"missing={missing_in_payload or '[]'}, extra={extra_in_payload or '[]'}"
        )

    validated: dict[str, str] = {}

    for entry in entries:
        value = payload.get(entry.name)
        if not isinstance(value, str) or not value.strip():
            raise SyncError(f"Translation value for key '{entry.name}' must be a non-empty string")

        source_placeholders = placeholder_counter(entry.text)
        translated_placeholders = placeholder_counter(value)
        if source_placeholders != translated_placeholders:
            raise SyncError(
                f"Placeholder mismatch for key '{entry.name}': "
                f"source={dict(source_placeholders)}, translated={dict(translated_placeholders)}"
            )

        validated[entry.name] = value

    return validated


def write_locale_tree(tree: ET.ElementTree, output_path: Path) -> None:
    output_path.parent.mkdir(parents=True, exist_ok=True)
    ET.indent(tree, space="    ")
    xml_body = ET.tostring(tree.getroot(), encoding="utf-8")
    content = b'<?xml version="1.0" encoding="utf-8"?>\n' + xml_body + b"\n"
    output_path.write_bytes(content)


def sync(config: Config, mode: str, repo_root: Path, token: str | None) -> dict[str, Any]:
    source_files = discover_source_files(repo_root, config)
    if not source_files:
        raise SyncError("No source strings.xml files matched the configured source_globs")

    results: list[FileLocaleResult] = []
    files_updated = 0
    missing_total = 0
    translated_total = 0

    for source_file in source_files:
        _, _, source_entries, _ = parse_string_entries(source_file)
        source_by_name = {entry.name: entry for entry in source_entries}

        for locale in config.target_locales:
            locale_file = locale_path_for(source_file, locale)
            locale_tree, locale_root, _, locale_by_name = initialize_locale_tree(locale_file)

            missing_keys = [entry.name for entry in source_entries if entry.name not in locale_by_name]
            missing_total += len(missing_keys)

            added_keys = 0
            if mode == "apply" and missing_keys:
                if not token:
                    raise SyncError("GITHUB_TOKEN is required in apply mode when missing keys exist")

                entries_for_translation = [source_by_name[name] for name in missing_keys]
                translated = generate_translations(
                    provider=config.provider,
                    token=token,
                    source_file=source_file,
                    locale=locale,
                    entries=entries_for_translation,
                )

                for key in missing_keys:
                    node = ET.SubElement(locale_root, "string", {"name": key})
                    node.text = translated[key]
                    added_keys += 1

                write_locale_tree(locale_tree, locale_file)
                files_updated += 1
                translated_total += added_keys

            results.append(
                FileLocaleResult(
                    source_file=source_file.relative_to(repo_root).as_posix(),
                    locale_file=locale_file.relative_to(repo_root).as_posix(),
                    locale=locale,
                    missing_keys=missing_keys,
                    added_keys=added_keys,
                )
            )

    report = {
        "mode": mode,
        "source_file_count": len(source_files),
        "target_locales": config.target_locales,
        "totals": {
            "missing_keys": missing_total,
            "translated_keys": translated_total,
            "files_updated": files_updated,
        },
        "results": [
            {
                "source_file": result.source_file,
                "locale_file": result.locale_file,
                "locale": result.locale,
                "missing_keys": result.missing_keys,
                "missing_count": len(result.missing_keys),
                "added_keys": result.added_keys,
            }
            for result in results
        ],
    }
    return report


def build_markdown_report(report: dict[str, Any]) -> str:
    mode = report["mode"]
    source_file_count = report["source_file_count"]
    totals = report["totals"]

    lines = [
        "# i18n Sync Report",
        "",
        f"- Mode: `{mode}`",
        f"- Source files discovered: `{source_file_count}`",
        f"- Missing keys detected: `{totals['missing_keys']}`",
        f"- Keys translated this run: `{totals['translated_keys']}`",
        f"- Locale files updated: `{totals['files_updated']}`",
        "",
        "## Missing Keys By File/Locale",
        "",
        "| Source file | Locale | Missing keys | Locale file |",
        "|---|---:|---:|---|",
    ]

    sorted_results = sorted(
        report["results"],
        key=lambda item: (item["source_file"], item["locale"]),
    )

    for item in sorted_results:
        lines.append(
            f"| `{item['source_file']}` | `{item['locale']}` | {item['missing_count']} | `{item['locale_file']}` |"
        )

    details_added = False
    for item in sorted_results:
        if not item["missing_keys"]:
            continue
        if not details_added:
            lines.extend(["", "## Missing Key Details", ""])
            details_added = True
        keys = ", ".join(f"`{key}`" for key in item["missing_keys"])
        lines.append(f"- `{item['source_file']}` (`{item['locale']}`): {keys}")

    if not details_added:
        lines.extend(["", "No missing keys found."])

    lines.append("")
    return "\n".join(lines)


def write_report(path: str | None, content: str, as_json: bool = False) -> None:
    if not path:
        return

    output_path = Path(path)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    if as_json:
        output_path.write_text(content + "\n", encoding="utf-8")
    else:
        output_path.write_text(content, encoding="utf-8")


def main() -> int:
    args = parse_args()
    repo_root = Path.cwd()

    config = load_config(Path(args.config))

    token = None
    if args.mode == "apply":
        token = os.environ.get("GITHUB_TOKEN")

    report = sync(config=config, mode=args.mode, repo_root=repo_root, token=token)

    report_json = json.dumps(report, indent=2, ensure_ascii=False)
    report_md = build_markdown_report(report)

    if args.report_json:
        write_report(args.report_json, report_json, as_json=True)
    if args.report_md:
        write_report(args.report_md, report_md, as_json=False)

    print(report_md)
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except SyncError as exc:
        print(f"ERROR: {exc}", file=sys.stderr)
        raise SystemExit(1)
