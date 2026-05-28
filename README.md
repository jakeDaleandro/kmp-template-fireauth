# KMP Firebase Auth Template

A Kotlin Multiplatform template with Firebase Authentication pre-configured for Android and iOS, built with Compose Multiplatform.

## What's Included

- **Compose Multiplatform UI** shared across Android and iOS
- **Firebase Authentication** with email/password sign-in and registration
- **Auth screens** — login, register, and success screen with animated transitions
- **expect/actual architecture** for platform-specific Firebase initialization
- **KTLint** for code style enforcement

## Platform Support

| Platform | Status |
|----------|--------|
| Android  | ✅ Full Firebase Auth |
| iOS      | ✅ Full Firebase Auth |
| Web (WASM) | ⚠️ Firebase not yet supported — stub implementation |

## Project Structure

```
kmp-template/
├── shared/                         # Shared KMP module
│   └── src/
│       ├── commonMain/             # Shared UI and logic (Compose, ViewModels)
│       │   └── kotlin/
│       │       ├── auth/
│       │       │   └── AuthService.kt      # expect class
│       │       └── ui/
│       │           └── AuthScreen.kt       # Login, Register, Success screens
│       ├── firebaseMain/           # Firebase actual implementation (Android + iOS + JS)
│       │   └── kotlin/auth/
│       │       └── AuthService.kt
│       ├── androidMain/            # Android-specific
│       ├── iosMain/                # iOS entry point
│       │   └── kotlin/
│       │       └── MainViewController.kt
│       ├── jsMain/                 # JS Firebase initialization
│       │   └── kotlin/auth/
│       │       └── FirebaseInit.kt
│       └── wasmJsMain/             # WASM stub
│           └── kotlin/auth/
│               └── AuthService.kt
├── androidApp/                     # Android application module
└── iosApp/                         # Xcode project
```

## Getting Started

### Prerequisites

- Android Studio Meerkat or later
- Xcode 15 or later
- A Firebase project ([console.firebase.google.com](https://console.firebase.google.com))

### Firebase Setup

1. Create a Firebase project and enable **Email/Password** authentication under **Authentication → Sign-in method**

2. **Android** — register your app and download `google-services.json` into `androidApp/`

3. **iOS** — register your app and download `GoogleService-Info.plist`, add it to the `iosApp` target in Xcode

4. **Web** — register a web app and update `shared/src/jsMain/kotlin/auth/FirebaseInit.kt` with your config:

```kotlin
fun initializeFirebase() {
    Firebase.initialize(
        options = FirebaseOptions(
            applicationId = "YOUR_APP_ID",
            apiKey = "YOUR_API_KEY",
            projectId = "YOUR_PROJECT_ID",
            storageBucket = "YOUR_STORAGE_BUCKET",
            gcmSenderId = "YOUR_SENDER_ID",
            authDomain = "YOUR_AUTH_DOMAIN"
        )
    )
}
```

### Android

Open the project in Android Studio and run the `androidApp` configuration.

### iOS

1. Add the Firebase iOS SDK via Swift Package Manager in Xcode:
   - **File → Add Package Dependencies**
   - URL: `https://github.com/firebase/firebase-ios-sdk`
   - Add **FirebaseAuth** to the `iosApp` target

2. Run the `iosApp` scheme in Xcode or from Android Studio with the KMP plugin

## Key Dependencies

| Dependency | Version |
|-----------|---------|
| Kotlin | 2.1.21 |
| Compose Multiplatform | 1.7.3 |
| GitLive Firebase KMP | 2.4.0 |
| AGP | 9.2.1 |
| Firebase BOM (Android) | 33.7.0 |

## Adding New Screens

All UI lives in `shared/src/commonMain/kotlin`. Add a new composable there and it will render on both Android and iOS automatically. The only platform-specific entry points are:

- **Android** — `androidApp/src/main/kotlin/.../MainActivity.kt`
- **iOS** — `shared/src/iosMain/kotlin/MainViewController.kt`

## Notes

- `google-services.json` and `GoogleService-Info.plist` are excluded from version control — each developer must add their own
- WASM web support for Firebase is pending upstream support from the GitLive SDK
- `kotlin.native.cacheKind=none` is set in `gradle.properties` to work around a Kotlin/Native compiler cache bug with Compose Foundation 1.7.3
