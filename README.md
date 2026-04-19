# UseeEar Reconstructed

This is a clean, Android Studio-compatible reconstruction of the UseeEar 1.0.062 APK focused on the Wi-Fi ear-camera connection path.

## What Is Included

- A minimal native-host Android app under package `com.i4season.i4season_camera`.
- The original JNI bridge classes needed by the newer Wi-Fi camera path:
  - `com.jni.WifiCamera`
  - `com.jni.WifiCameraApi`
  - `com.jni.CallBack.WifiCallBack`
  - `com.jni.CallBack.WifiCallBackFuc`
  - `com.jni.WifiCameraInfo.*`
- Original native libraries for `arm64-v8a` and `armeabi-v7a`, placed under `app/src/main/jniLibs`.
- A hand-written `MainActivity` that requests permissions, opens Wi-Fi settings, starts/stops the native camera stack, displays connection state, queries firmware/status info, and previews incoming JPEG frames.

## Connection Flow

The APK's newer UseeEar path is Wi-Fi based, not BLE-first.

The important decompiled path is:

1. `WifiCameraApi.getInstance().init(context)`
2. `WifiCallBackFuc.getInstance().setContext(context)`
3. `WifiCallBack.getInstance().loop()`
4. `WifiCamera.caInit()`
5. `WifiCamera.caStart()`
6. Native callbacks into `WifiCallBack.sendsig`, `sendpic`, `sendaudio`, and `sendstatus`

The original app also recognizes these device SSID prefixes:

- `UseeEar`
- `i4season`
- `inskam`
- `Yanxuan`

The default documented password constant in the APK is `12345678`.

## Build

From this folder:

```powershell
.\gradlew.bat :app:assembleDebug
```

Output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Notes

The full JADX output is kept separately in `../reverse/jadx-useeear`. That export is useful for reference, but it includes decompiled AndroidX/AppCompat/ConstraintLayout sources and malformed methods, so this clean reconstruction intentionally keeps only the native connection bridge and replaces the UI with readable source.

## Security Posture

This reconstruction removes the original Java-side Simicloud license/report flow, settings WebView screens, phone-ID utility surface, installed-package enumeration utility surface, and account/binding/license-burning UI paths.

Residual risk remains because the native Wi-Fi camera libraries are still original binaries. They are kept because the device connection path depends on them. For higher assurance, run the debug APK with network capture and block `*.simicloud.com` while verifying that camera connection and frame preview still work.
