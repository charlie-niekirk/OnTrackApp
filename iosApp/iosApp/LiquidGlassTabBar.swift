import SwiftUI
import ComposeApp

private enum AppThemeMode: String {
    case system = "SYSTEM"
    case light = "LIGHT"
    case dark = "DARK"
}

@MainActor
private final class ThemeModeSync: ObservableObject {
    @Published var preferredColorScheme: ColorScheme?
    @Published var tintColor = Color(red: 127.0 / 255.0, green: 174.0 / 255.0, blue: 214.0 / 255.0)

    private let bridge = ThemeModeBridge()
    private lazy var observer = SwiftThemeModeObserver { [weak self] modeName in
        self?.apply(modeName: modeName)
    }

    init() {
        bridge.start(observer: observer)
    }

    deinit {
        bridge.dispose()
    }

    private func apply(modeName: String) {
        let mode = AppThemeMode(rawValue: modeName) ?? .system

        switch mode {
        case .system:
            preferredColorScheme = nil
            tintColor = Color(red: 127.0 / 255.0, green: 174.0 / 255.0, blue: 214.0 / 255.0)
        case .light:
            preferredColorScheme = .light
            tintColor = Color(red: 127.0 / 255.0, green: 174.0 / 255.0, blue: 214.0 / 255.0)
        case .dark:
            preferredColorScheme = .dark
            tintColor = Color(red: 166.0 / 255.0, green: 204.0 / 255.0, blue: 233.0 / 255.0)
        }
    }
}

private final class SwiftThemeModeObserver: NSObject, ThemeModeObserver {
    private let onChanged: (String) -> Void

    init(onChanged: @escaping (String) -> Void) {
        self.onChanged = onChanged
        super.init()
    }

    func onThemeModeChanged(modeName: String) {
        DispatchQueue.main.async { [onChanged] in
            onChanged(modeName)
        }
    }
}

struct MainTabView: View {
    @State private var selectedTab = 0
    @StateObject private var themeModeSync = ThemeModeSync()

    var body: some View {
        TabView(selection: $selectedTab) {
            SearchTabView()
                .ignoresSafeArea()
                .tabItem {
                    Label("Search", systemImage: "magnifyingglass")
                }
                .tag(0)

            PinnedTabView()
                .ignoresSafeArea()
                .tabItem {
                    Label("Pinned", systemImage: "pin.fill")
                }
                .tag(1)

            SettingsTabView()
                .ignoresSafeArea()
                .tabItem {
                    Label("Settings", systemImage: "gearshape.fill")
                }
                .tag(2)
        }
        .preferredColorScheme(themeModeSync.preferredColorScheme)
        .tint(themeModeSync.tintColor)
    }
}

struct SearchTabView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        TabViewControllersKt.SearchTabViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct PinnedTabView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        TabViewControllersKt.PinnedTabViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct SettingsTabView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        TabViewControllersKt.SettingsTabViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
