# fGuard – Facebook Blocker via Accessibility Service

**fGuard** is an Android accessibility service that helps you stay focused by preventing indirect access to Facebook. It smartly blocks Facebook when launched directly or through in-app browsers like Messenger’s "Facebook" button (which opens Facebook inside a WebView).

## ✨ Features

- 🔒 **Blocks Facebook App** from launching directly.
- 🚫 **Blocks Messenger's Facebook shortcut** that opens Facebook in a WebView.
- ⚙️ Uses AccessibilityService to detect view interactions and app launches.
- 💥 Automatically triggers a back action to close Facebook views instantly.

## 🔧 How It Works

- Monitors accessibility events (especially `TYPE_VIEW_CLICKED` and window content).
- Detects when:
  - Facebook app (`com.facebook.katana`) is launched.
  - A view with contentDescription `"Facebook App"` is clicked (used by Messenger's WebView shortcut).
- Triggers a back action to instantly close the Facebook view.

<table>
  <tr>
    <td>Without Facebook App</td>
    <td>With Facebook App</td>
  </tr>
  <tr>
    <td>
      <video src="https://github.com/user-attachments/assets/b423bca8-2cd7-476e-a5b3-89931749afb1"></video>
    </td>
    <td>
      <video src="https://github.com/user-attachments/assets/9305d94e-eef0-439b-980b-3f93f4db7d13"></video>
    </td>
  </tr>
</table>

## 🛠️ Setup & Usage

1. **Install the app on your device.**
2. **Enable Accessibility Service** for `fGuard`:
   - Click the "Start" button in the app UI.
   - You’ll be redirected to the Accessibility Settings screen.
3. **Once active**, the service will:
   - Automatically block Facebook app launches.
   - Block indirect Facebook access via Messenger’s WebView.

## 📦 Tech Stack

- Kotlin + Jetpack Compose
- Android AccessibilityService
- Coroutine + Mutex for async safety

## 📱 UI Overview

- Minimal Compose UI
- Shows current service status (running/not running)
- Circular button to toggle Accessibility Settings

## 📝 License

This project is open-source and available under the [MIT License](LICENSE).

---

> Built to help you stay distraction-free. Block Facebook smartly and focus better. 🚀
