# UseeEar 1.0.062 Decompiled Reference

This branch contains a local JADX decompilation of the APK:

- App label: `UseeEar`
- Package: `com.i4season.i4season_camera`
- Version: `1.0.062`, versionCode `44`
- Source APK: `UseeEar_1.0.062_APKPure.apk`
- Decompiler: JADX CLI with Gradle export

This tree is intended for security review and reconstruction work. It is not a clean upstream source release. Some methods are malformed by decompilation, and the full exported Gradle project does not currently compile without deeper repair.

## Security Report

### Executive Summary

I did not find a clear smoking-gun spyware module in the Java code, such as SMS theft, contact scraping, call-log collection, accessibility abuse, device-admin persistence, boot persistence, or dynamic dex loading.

The original APK is still privacy-sensitive. The most important finding is a license/activation reporting path that posts device, app, phone model, Android ID/random UUID, firmware, serial, MAC, vendor, product, and license data to Simicloud cloud endpoints. Several native libraries also contain additional Simicloud API paths that cannot be fully audited from Java alone.

For a privacy-minimized build, the reconstructed branch removes the Java-side telemetry/reporting flow and keeps only the Wi-Fi camera connection bridge. However, it still includes the original native camera libraries because those are likely required for the ear-camera connection, so native-library network behavior remains a residual risk unless the native protocol is fully reimplemented or sandboxed.

### Suspicious Cloud Endpoints

These are the endpoints and routes that deserve the most attention:

| Endpoint or route | Source | Risk |
| --- | --- | --- |
| `https://yun.simicloud.com/licplatform/user/v2/report` | `ReportLicInstance.java` | Active Java HTTP POST used by the license/report flow. Sends persistent device/app/firmware metadata. |
| `https://lic.simicloud.com/api/nto/miss?` | `FunctionSwitch.java` | Online license burning endpoint constant. Suspicious because it appears tied to remote license provisioning. |
| `http://www.simicloud.com/c/UStorage/android/version.xml` | `Constant.java` | Cleartext update/version endpoint. Integrity and privacy risk if used over hostile networks. |
| `lic.simicloud.com` | Native strings in `libI4Tool.so` and `libUStorageDeviceFS.so` | Native code contains cloud licensing domain. Runtime behavior is opaque without native reversing or traffic capture. |
| `info2.simicloud.com` | Native strings in `libI4Tool.so` / `libUStorageDeviceFS.so` | Likely reporting host. Needs network capture to confirm call sites and payloads. |
| `https://%s/api/nto/q` | Native strings | Parameterized Simicloud-style API route. Host may be set dynamically. |
| `https://%s/api/nto/qlic2` | Native strings | License query route. |
| `https://%s/api/nto/qlic3` | Native strings | License query route. |
| `https://%s/api/nto/qmisslic2` | Native strings | Missing-license reporting/query route. |
| `https://lic.simicloud.com/api/nto/error` | Native strings | Error telemetry route. |
| `/api/nto/checkmiss?platform=sdk` | Native strings | SDK license/missing-license route. |
| `/api/storage/license/idlist` | Native strings | Storage license route. |
| `/api/storage/status/report` | Native strings | Status reporting route. |
| `/api/storage/status/reuse` | Native strings | Status/reuse route. |
| `https://%s/api/storage/license/product_list?f=%d&b=%d&p=%d` | Native strings | Parameterized storage license route. |
| `https://%s/api/storage/order/query?orderno=%s` | Native strings | License/order query route. |
| `https://%s/api/storage/request/license?orderno=%s&num=%d` | Native strings | License request route. |
| `https://%s/api/storage/license/replyinfo?orderno=%s` | Native strings | License reply route. |

Local camera/device URLs also appear and are expected for this class of Wi-Fi otoscope:

| Endpoint | Source | Assessment |
| --- | --- | --- |
| `http://192.168.1.254/DCIM/MOVIE/%s` | `libWifiCamera.so` | Local camera media route. Not cloud telemetry by itself. |
| `http://192.168.1.254/DCIM/PHOTO/%s` | `libWifiCamera.so` | Local camera media route. Not cloud telemetry by itself. |
| `http://10.10.10.254:` | `AppPathInfo.java` | Local Wi-Fi device base URL. |
| `http://127.0.0.1:` | `AppPathInfo.java` | Local loopback storage/proxy URL. |

### Confirmed Java Telemetry Flow

`I4seasonCameraNew.wifiLicenseCheck(...)` calls:

```text
ReportLicInstance.getInstance().licProcessBegin(...)
```

That path is reached when the native/license check does not return success. It sends a report to:

```text
https://yun.simicloud.com/licplatform/user/v2/report
```

The code has two network paths:

- `requestNetwork2Wifi(...)`: normal `HttpURLConnection`
- `requestLicCheck(...)`: `Network.openConnection(...)` after explicitly requesting a cellular network

The cellular-network request is notable: the code builds a `NetworkRequest`, adds Internet capability, adds cellular transport, removes Wi-Fi transport, and then posts the same report through the cellular `Network` if available. This looks designed to reach the cloud even while Wi-Fi is connected to the local camera.

### Reported Payload Fields

The Java JSON payload includes:

- `lic`
- `uuid`: Android ID from `Settings.System.getString(..., "android_id")`, or a persisted random numeric fallback
- `sn`: device serial number
- `mac`: device MAC from camera firmware info
- `vendor`
- `product`
- `firmwareVersion`
- `appName`
- `appVersion`
- `appPacketName`
- `phoneProducer`: Android brand
- `phoneModel`
- `phoneVersion`: Android release

This is not merely a crash ping. It links app identity, phone identity/model data, and ear-camera hardware identity in one server request.

### Permissions

The APK requests:

- `INTERNET`
- `ACCESS_NETWORK_STATE`
- `ACCESS_WIFI_STATE`
- `CHANGE_WIFI_STATE`
- `CHANGE_WIFI_MULTICAST_STATE`
- `CHANGE_NETWORK_STATE`
- `ACCESS_COARSE_LOCATION`
- `ACCESS_FINE_LOCATION`
- `WRITE_EXTERNAL_STORAGE` with maxSdk 32
- `READ_EXTERNAL_STORAGE` with maxSdk 32
- `READ_MEDIA_IMAGES`
- `READ_MEDIA_VIDEO`
- `RECORD_AUDIO`

For a Wi-Fi camera app, network and Wi-Fi permissions are expected. Location permissions are often required by Android to scan/connect to Wi-Fi SSIDs, but they still increase privacy sensitivity. `RECORD_AUDIO` may be related to camera audio support; it should be reviewed against actual device capability.

Notably absent from the manifest:

- `READ_CONTACTS`
- `WRITE_CONTACTS`
- `READ_SMS`
- `SEND_SMS`
- `READ_CALL_LOG`
- `READ_PHONE_STATE`
- `QUERY_ALL_PACKAGES`
- `BIND_ACCESSIBILITY_SERVICE`
- `DEVICE_ADMIN`
- boot receiver persistence permissions

### Device Identifiers and Package Enumeration

The code contains utility methods that call `TelephonyManager.getDeviceId()` in `SystemUtil.java` and `UtilTools.java`. Because the manifest does not request `READ_PHONE_STATE`, these calls should fail or return limited values on modern Android versions unless permission behavior is different on old devices.

`UtilTools.getInstalledPackages(...)` enumerates installed packages, but the manifest does not request `QUERY_ALL_PACKAGES`, so package visibility is limited on modern Android. This is still worth noting because package enumeration is privacy-sensitive if it becomes reachable on older devices.

### WebView Risk

`WsLoadWebActivity.java` enables JavaScript and DOM storage, then loads a URL passed through an Intent extra. I did not find `addJavascriptInterface`, which reduces risk. The risk depends on whether untrusted callers can start that Activity with arbitrary URLs. The Activity is not marked exported in the manifest, so the risk appears mostly internal unless another path exposes it.

### Native Library Risk

Native code is the biggest unresolved area:

- `libWifiCamera.so` implements the local camera HTTP/control path.
- `libI4Tool.so` and `libUStorageDeviceFS.so` contain licensing/reporting strings, OpenSSL code, Simicloud domains, parameterized API routes, and JSON report templates.
- Native strings include a payload template with fields such as `Sn`, `License`, `Devname`, `Uuid`, `Platform`, `Mobile`, `Sysver`, `Appname`, `Appver`, `Ssid`, `Devip`, `Devport`, `Errid`, `Errtime`, `FistFp`, and `ErrEndTimes`.

Because these native binaries are not source code, static Java review cannot prove they do not send data. Any production use should verify native behavior with controlled dynamic analysis.

### Cleartext Traffic

The app contains cleartext HTTP local camera routes, which are expected for many Wi-Fi camera devices. The concerning cleartext endpoint is the cloud version URL:

```text
http://www.simicloud.com/c/UStorage/android/version.xml
```

If used, this could be observed or modified on the network unless separately authenticated. It should be removed or replaced with HTTPS plus signature verification in any maintained fork.

### Connection-Relevant Findings

The important device connection constants are:

- SSID prefixes: `i4season`, `UseeEar`, `inskam`, `Yanxuan`
- Default password constant: `12345678`
- Newer Wi-Fi camera path: `WifiCameraApi.init(...)`, `WifiCamera.caInit()`, `WifiCamera.caStart()`, callbacks through `WifiCallBack`

The reconstructed branch preserves this connection path while removing the original settings/account/license-report UI.

### Risk Rating

| Area | Rating | Reason |
| --- | --- | --- |
| Java spyware indicators | Low | No SMS/contact/call-log/accessibility/persistence/dynamic-loader behavior found. |
| Cloud telemetry/privacy | High | Active Java report flow posts persistent app, phone, firmware, serial, MAC, vendor, product, and license fields to Simicloud. |
| Native-library opacity | Medium-High | Native binaries contain additional Simicloud routes and payload templates. Behavior needs traffic capture or native reversing. |
| Network security | Medium | Local cleartext camera traffic may be expected; cleartext Simicloud version URL is not ideal. |
| Permissions | Medium | Wi-Fi/location/media/audio permissions are broad but partly explainable by camera operation. |
| Reconstructed project residual risk | Medium | Java telemetry removed, but original native libraries remain for connection. |

### Recommended Hardening

1. Remove or disable `ReportLicInstance` and all Simicloud license/report calls.
2. Block `*.simicloud.com` and `www.simicloud.com` at the app/network layer unless license activation is explicitly needed and documented.
3. Capture traffic from the original APK and reconstructed APK while connecting to the device, with and without Internet access.
4. Replace native libraries with clean protocol implementations where possible, especially the local camera HTTP/control protocol.
5. Keep only permissions required for the minimal camera path.
6. Keep cloud/update/version checks out of the reconstructed app unless a signed, auditable update mechanism is added.
7. Verify the app can connect while the Android device has no cellular data and no Internet route, only the UseeEar Wi-Fi network.

### Reconstructed Branch Security Delta

The reconstructed branch removes these Java-side risk areas:

- `ReportLicInstance` and the Java Simicloud report POST flow
- Settings/about/legal WebView screens
- Utility code that queries phone IDs and installed packages
- Account/binding/license-burning UI paths
- Broad decompiled app framework surface not needed for connection

It retains:

- The JNI bridge classes required for Wi-Fi camera connection
- The original ARM native camera libraries required by `WifiCamera`
- A minimal Activity for permission requests, Wi-Fi settings, start/stop, status callbacks, and frame preview

The result should be materially less privacy-invasive than the original Java app, but not cryptographically guaranteed clean until the native `.so` behavior is replaced or audited dynamically.

## Build Status

The JADX export was partially repaired enough to pass resource processing, but Java compilation still fails because the full decompiled tree contains malformed constructs such as `??` tokens in decompiled library and app classes. Use this branch for reference and audit, not as the clean build target.

The clean Android Studio-compatible build is on the reconstructed branch.
