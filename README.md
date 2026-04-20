# SecureeEar

SecureeEar is a clean Android reconstruction focused on local-only operation for UseeEar-compatible Wi-Fi ear hygiene cameras.

![SecureeEar secure ear camera illustration](docs/assets/secureeear-hero.png)

## Security Model

- No cloud APIs.
- No analytics.
- No WebView.
- No account system.
- No license reporting.
- No cellular fallback request.
- No phone identifier collection.
- No installed-app enumeration.
- No storage/media/audio permissions.

The app keeps the native Wi-Fi camera bridge behind a small Java controller and patches the native callback helper so the previous external-network bind path is refused. The remaining native `.so` files are opaque inherited components, so runtime network capture is still recommended before trusting any production deployment.

## Local Device Support

Expected SSID prefixes:

- `UseeEar`
- `i4season`
- `inskam`
- `Yanxuan`

Allowed local hosts:

- `192.168.1.254`
- `10.10.10.254`
- `192.168.1.1`
- `127.0.0.1`

## Build

```powershell
.\gradlew.bat :app:assembleDebug
```

Debug APK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Verification Checklist

1. Connect the Android device to the camera Wi-Fi network.
2. Disable cellular data or block it with a firewall.
3. Block `*.simicloud.com`.
4. Start SecureeEar and verify preview frames arrive.
5. Capture traffic and confirm connections remain local.
