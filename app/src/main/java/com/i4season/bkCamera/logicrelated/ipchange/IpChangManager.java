package com.i4season.bkCamera.logicrelated.ipchange;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.i4season.bkCamera.uirelated.other.i4seasonUtil.UtilTools;
import com.i4season.bkCamera.uirelated.other.logmanage.LogWD;
import com.jni.CallBack.UCallBack;
import java.util.ArrayList;
import java.util.List;

public class IpChangManager {
    private static final String EMPTY_IP = "0.0.0.0";
    private AcceptWiFiIpRunnable mAcceptWiFiIpRunnable;
    private Thread mThread;
    private String mIpAddress = "";
    private String mConnectedWifiId = "";
    private List<IpListenerDelagate> mWifiListenerDelagateList = new ArrayList();

    public static class WifiChangManagerWDHolder {
        public static IpChangManager logManager = new IpChangManager();
    }

    public static IpChangManager getInstance() {
        return WifiChangManagerWDHolder.logManager;
    }

    public void addWifiChangeListener(IpListenerDelagate ipListenerDelagate) {
        if (this.mWifiListenerDelagateList.contains(ipListenerDelagate)) {
            return;
        }
        this.mWifiListenerDelagateList.add(ipListenerDelagate);
    }

    public void removerWifiChangeListener(IpListenerDelagate ipListenerDelagate) {
        if (this.mWifiListenerDelagateList.contains(ipListenerDelagate)) {
            this.mWifiListenerDelagateList.remove(ipListenerDelagate);
        }
    }

    public void init(Context context) {
        LogWD.writeMsg(this, 8, "注册广播");
        LogWD.writeMsg(this, 8, "未开启子线程");
        this.mAcceptWiFiIpRunnable = new AcceptWiFiIpRunnable(context);
        this.mThread = new Thread(this.mAcceptWiFiIpRunnable);
        this.mThread.start();
    }

    public void findIpChange() {
        LogWD.writeMsg(this, 8, "ip发生变化  发送所有需要监听者 ");
        sendIpChangeObserver();
    }

    private void sendIpChangeObserver() {
        LogWD.writeMsg(this, 8, "IP变化： " + this.mWifiListenerDelagateList.size());
        for (int i = 0; i < this.mWifiListenerDelagateList.size(); i++) {
            this.mWifiListenerDelagateList.get(i).onIpChangeListener("", this.mIpAddress);
        }
    }

    public void findTryagain() {
        LogWD.writeMsg(this, 8, "重试 ");
        sendTryagainObserver();
    }

    private void sendTryagainObserver() {
        LogWD.writeMsg(this, 8, "重试： " + this.mWifiListenerDelagateList.size());
        for (int i = 0; i < this.mWifiListenerDelagateList.size(); i++) {
            this.mWifiListenerDelagateList.get(i).onIpChangeListener("", this.mIpAddress);
        }
    }

    public void emptyIpHandler() {
        LogWD.writeMsg(this, 8, "空IP处理：  " + this.mWifiListenerDelagateList.size());
        for (int i = 0; i < this.mWifiListenerDelagateList.size(); i++) {
            this.mWifiListenerDelagateList.get(i).onEmptyIpListener();
        }
    }

    public class AcceptWiFiIpRunnable implements Runnable {
        public static final int LOOP_MAX = 200;
        private Context mContext;
        private int mLoopNumber = 200;
        private int MAXTRYTIME = 50;
        private int MAXDELAY = 80;
        private int times = 0;

        public AcceptWiFiIpRunnable(Context context) {
            this.mContext = context;
        }

        private void resetTryagain() {
            this.times = 0;
        }

        private void tryagain() {
            if (UCallBack.getInstance().online) {
                LogWD.writeMsg(this, 8, "tryagain  已经在线  无需重连");
                this.times = 50;
                return;
            }
            LogWD.writeMsg(this, 8, "tryagain times=" + this.times);
            int i = this.times;
            int i2 = this.MAXTRYTIME;
            int i3 = this.MAXDELAY;
            if (i > i2 * i3) {
                LogWD.writeMsg(this, 8, "tryagain 已达到最大重试次数");
                return;
            }
            this.times = i + 1;
            if (this.times % i3 == 0) {
                LogWD.writeMsg(this, 8, "tryagain 开始重连  当前次数：" + (this.times / this.MAXDELAY));
                IpChangManager.this.findTryagain();
            }
        }

        @Override
        public void run() {
            IpChangManager.this.mIpAddress = UtilTools.getWifiRouteIPAddress(this.mContext);
            IpChangManager.this.mConnectedWifiId = UtilTools.getConnectedWifiId(this.mContext);
            SystemClock.sleep(1000L);
            while (true) {
                LogWD.writeMsg(this, 8, "IP mIpAddress: " + IpChangManager.this.mIpAddress + "   mConnectedWifiId: " + IpChangManager.this.mConnectedWifiId);
                String wifiRouteIPAddress = UtilTools.getWifiRouteIPAddress(this.mContext);
                String connectedWifiId = UtilTools.getConnectedWifiId(this.mContext);
                LogWD.writeMsg(this, 8, "IP ipAddress: " + wifiRouteIPAddress + "   connectedWifiId: " + connectedWifiId);
                if (IpChangManager.EMPTY_IP.equals(wifiRouteIPAddress)) {
                    LogWD.writeMsg(this, 8, "IP 为空");
                    resetTryagain();
                    if (!IpChangManager.EMPTY_IP.equals(IpChangManager.this.mIpAddress)) {
                        IpChangManager.this.emptyIpHandler();
                        IpChangManager.this.mIpAddress = wifiRouteIPAddress;
                        SystemClock.sleep(100L);
                    } else {
                        SystemClock.sleep(100L);
                    }
                } else if (!TextUtils.isEmpty(connectedWifiId)) {
                    if (!IpChangManager.this.mIpAddress.equals(wifiRouteIPAddress)) {
                        LogWD.writeMsg(this, 8, "ip发生变化  wifi有变化   current ssid: " + UtilTools.acceptCurrentWifiId(this.mContext));
                        if (!IpChangManager.EMPTY_IP.equals(IpChangManager.this.mIpAddress)) {
                            IpChangManager.this.emptyIpHandler();
                            SystemClock.sleep(100L);
                        }
                        IpChangManager.this.mIpAddress = wifiRouteIPAddress;
                        IpChangManager.this.mConnectedWifiId = connectedWifiId;
                        IpChangManager.this.findIpChange();
                        resetTryagain();
                    } else {
                        LogWD.writeMsg(this, 8, "ip未发生变化 检测设备状态是否需要重连");
                        tryagain();
                    }
                    SystemClock.sleep(100L);
                } else {
                    LogWD.writeMsg(this, 8, "mConnectedWifiId  为空");
                    resetTryagain();
                    SystemClock.sleep(100L);
                }
            }
        }

        public void setmLoopNumber(int i) {
            this.mLoopNumber = i;
        }
    }
}
