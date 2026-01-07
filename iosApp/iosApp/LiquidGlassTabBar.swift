import SwiftUI
import ComposeApp

struct MainTabView: View {
    @State private var selectedTab = 0

    var body: some View {
        TabView(selection: $selectedTab) {
            SearchTabView()
                .ignoresSafeArea()
                .tabItem {
                    Label("Search", systemImage: "magnifyingglass")
                }
                .tag(0)

            FavouritesTabView()
                .ignoresSafeArea()
                .tabItem {
                    Label("Favourites", systemImage: "heart.fill")
                }
                .tag(1)

            SettingsTabView()
                .ignoresSafeArea()
                .tabItem {
                    Label("Settings", systemImage: "gearshape.fill")
                }
                .tag(2)
        }
    }
}

struct SearchTabView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        TabViewControllersKt.SearchTabViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct FavouritesTabView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        TabViewControllersKt.FavouritesTabViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct SettingsTabView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        TabViewControllersKt.SettingsTabViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
