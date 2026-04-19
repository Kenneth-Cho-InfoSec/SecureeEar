package com.i4season.bkCamera.uirelated.other.i4seasonUtil;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import com.jni.UStorageDeviceModule;
import java.util.Iterator;

public class WiFiUtil {
    private static final int WIFICIPHER_NOPASS = 0;
    private static final int WIFICIPHER_WEP = 1;
    private static final int WIFICIPHER_WPA = 2;
    private static final WiFiUtil ourInstance = new WiFiUtil();
    private WifiManager mWifiManager;

    public static WiFiUtil getIns() {
        return ourInstance;
    }

    public void init(Context context) {
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
    }

    public boolean changeToWifi(String str, String str2) {
        boolean z;
        int security;
        WifiConfiguration wifiConfigurationCreateWifiConfig;
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null) {
            return false;
        }
        wifiManager.startScan();
        SystemClock.sleep(200L);
        Iterator<ScanResult> it = this.mWifiManager.getScanResults().iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                security = UStorageDeviceModule.ERROR_NO_SN;
                break;
            }
            ScanResult next = it.next();
            if (next.SSID.contains(str)) {
                str = next.SSID;
                z = true;
                security = getSecurity(next);
                break;
            }
        }
        if (!z) {
            return false;
        }
        printCurWifiInfo();
        if (this.mWifiManager.getConfiguredNetworks() == null) {
            return false;
        }
        if (security != -1001) {
            wifiConfigurationCreateWifiConfig = createWifiInfo(str, str2, security);
        } else {
            wifiConfigurationCreateWifiConfig = createWifiConfig(str, str2, 0);
        }
        int iAddNetwork = this.mWifiManager.addNetwork(wifiConfigurationCreateWifiConfig);
        if (iAddNetwork == -1) {
            return false;
        }
        return doChange2Wifi(iAddNetwork);
    }

    public boolean changeToItenertWifi(String str, String str2) {
        int security;
        WifiConfiguration wifiConfigurationCreateWifiInfo;
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null) {
            return false;
        }
        wifiManager.startScan();
        SystemClock.sleep(200L);
        Iterator<ScanResult> it = this.mWifiManager.getScanResults().iterator();
        while (true) {
            if (!it.hasNext()) {
                security = UStorageDeviceModule.ERROR_NO_SN;
                break;
            }
            ScanResult next = it.next();
            if (next.SSID.contains(str)) {
                security = getSecurity(next);
                break;
            }
        }
        String str3 = "\"" + str + "\"";
        printCurWifiInfo();
        if (security != -1001) {
            wifiConfigurationCreateWifiInfo = createWifiInfo(str, str2, security);
        } else {
            wifiConfigurationCreateWifiInfo = createWifiInfo(str, str2);
        }
        int iAddNetwork = this.mWifiManager.addNetwork(wifiConfigurationCreateWifiInfo);
        if (iAddNetwork == -1) {
            return false;
        }
        return doChange2Wifi(iAddNetwork);
    }

    private boolean doChange2Wifi(int i) {
        if (!this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(true);
        }
        return this.mWifiManager.enableNetwork(i, true);
    }

    private WifiConfiguration createWifiInfo(String str, String str2) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"" + str + "\"";
        wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
        wifiConfiguration.BSSID = "any";
        wifiConfiguration.FQDN = null;
        wifiConfiguration.allowedGroupCiphers.set(2);
        wifiConfiguration.allowedGroupCiphers.set(3);
        wifiConfiguration.allowedKeyManagement.set(0);
        wifiConfiguration.allowedPairwiseCiphers.set(1);
        wifiConfiguration.allowedPairwiseCiphers.set(2);
        wifiConfiguration.allowedProtocols.set(0);
        wifiConfiguration.allowedProtocols.set(1);
        wifiConfiguration.status = 2;
        return wifiConfiguration;
    }

    private WifiConfiguration createWifiConfig(String str, String str2, int i) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"" + str + "\"";
        WifiConfiguration wifiConfigurationIsExist = isExist(str);
        if (wifiConfigurationIsExist != null) {
            this.mWifiManager.removeNetwork(wifiConfigurationIsExist.networkId);
        }
        if (i == 0) {
            wifiConfiguration.allowedKeyManagement.set(0);
        } else if (i == 1) {
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.wepKeys[0] = "\"" + str2 + "\"";
            wifiConfiguration.allowedAuthAlgorithms.set(0);
            wifiConfiguration.allowedAuthAlgorithms.set(1);
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.wepTxKeyIndex = 0;
        } else if (i == 2) {
            wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.allowedAuthAlgorithms.set(0);
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedKeyManagement.set(1);
            wifiConfiguration.allowedPairwiseCiphers.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedPairwiseCiphers.set(2);
            wifiConfiguration.status = 2;
        }
        return wifiConfiguration;
    }

    private WifiConfiguration isExist(String str) {
        for (WifiConfiguration wifiConfiguration : this.mWifiManager.getConfiguredNetworks()) {
            if (wifiConfiguration.SSID.equals("\"" + str + "\"")) {
                return wifiConfiguration;
            }
        }
        return null;
    }

    public void printCurWifiInfo() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null) {
            return;
        }
        wifiManager.getConnectionInfo();
    }

    public boolean closeWifi(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).setWifiEnabled(false);
    }

    public boolean openWifi(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).setWifiEnabled(true);
    }

    public WifiConfiguration createWifiInfo(String str, String str2, int i) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"" + str + "\"";
        if (i == 0) {
            wifiConfiguration.wepKeys[0] = "\"\"";
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.wepTxKeyIndex = 0;
        } else if (i == 1) {
            wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.allowedAuthAlgorithms.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedGroupCiphers.set(0);
            wifiConfiguration.allowedGroupCiphers.set(1);
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.wepTxKeyIndex = 0;
        } else {
            if (i != 2) {
                return null;
            }
            wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.allowedAuthAlgorithms.set(0);
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedKeyManagement.set(1);
            wifiConfiguration.allowedKeyManagement.set(2);
            wifiConfiguration.allowedPairwiseCiphers.set(1);
            wifiConfiguration.allowedProtocols.set(0);
            wifiConfiguration.allowedProtocols.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedPairwiseCiphers.set(2);
            wifiConfiguration.status = 2;
        }
        return wifiConfiguration;
    }

    public int getSecurity(ScanResult scanResult) {
        if (scanResult.capabilities.contains("WEP")) {
            return 1;
        }
        if (scanResult.capabilities.contains("PSK")) {
            return 2;
        }
        return scanResult.capabilities.contains("EAP") ? 3 : 0;
    }
}
