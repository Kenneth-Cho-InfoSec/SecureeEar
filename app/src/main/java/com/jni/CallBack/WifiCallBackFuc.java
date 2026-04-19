package com.jni.CallBack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.SystemClock;
import com.jni.WifiCameraApi;
import java.io.FileDescriptor;
import java.lang.reflect.Field;

public class WifiCallBackFuc {
    public boolean breakbindflag;
    public Context gcontext;
    public Network gnetwork;
    public Network gnetworkout;
    public String name;
    public String name2;
    public int start;

    public static class conConfigHolder {
        public static WifiCallBackFuc gWifiCallBackFuc = new WifiCallBackFuc();
    }

    public void fdtowifi(int i) {
    }

    private WifiCallBackFuc() {
        this.start = 0;
        this.breakbindflag = false;
        this.name = "allpacket";
        this.name2 = "allproduct";
    }

    public void breakbind() {
        this.breakbindflag = true;
    }

    public int bindSocketToNetwork(int i) {
        if (this.breakbindflag) {
            return 0;
        }
        if (this.gnetwork != null && Build.VERSION.SDK_INT >= 23) {
            FileDescriptor fileDescriptor = new FileDescriptor();
            try {
                Field declaredField = FileDescriptor.class.getDeclaredField("descriptor");
                declaredField.setAccessible(true);
                declaredField.setInt(fileDescriptor, i);
                this.gnetwork.bindSocket(fileDescriptor);
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public int bindSocketToNetworkOut(int i) {
        if (this.breakbindflag) {
            return 0;
        }
        if (this.gnetworkout != null && Build.VERSION.SDK_INT >= 23) {
            FileDescriptor fileDescriptor = new FileDescriptor();
            try {
                Field declaredField = FileDescriptor.class.getDeclaredField("descriptor");
                declaredField.setAccessible(true);
                declaredField.setInt(fileDescriptor, i);
                this.gnetworkout.bindSocket(fileDescriptor);
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    private void forceSendRequestByMobileData(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (Build.VERSION.SDK_INT >= 23) {
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                builder.addCapability(12);
                builder.addTransportType(1);
                builder.removeTransportType(0);
                connectivityManager.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        WifiCallBackFuc.getInstance().setNetwork(network);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void forceSendRequestByMobileDataOut(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (Build.VERSION.SDK_INT >= 23) {
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                builder.addCapability(12);
                builder.addTransportType(0);
                builder.removeTransportType(1);
                connectivityManager.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        WifiCallBackFuc.getInstance().setNetworkOut(network);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized String getPackageName(Context context) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
    }

    public String getString() {
        return this.name;
    }

    public String getString2() {
        return this.name2;
    }

    public String getString3() {
        return getPackageName(this.gcontext);
    }

    public void setContext(Context context) {
        if (this.gcontext == null) {
            this.gcontext = context;
            if (!this.breakbindflag) {
                forceSendRequestByMobileData(context);
                forceSendRequestByMobileDataOut(context);
            }
        }
        loop();
    }

    public void setNetwork(Network network) {
        this.gnetwork = network;
    }

    public void setNetworkOut(Network network) {
        this.gnetworkout = network;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.jni.CallBack.WifiCallBackFuc$3] */
    public void loop() {
        if (this.start == 0) {
            this.start = 1;
            new Thread() {
                @Override
                public void run() {
                    WifiCameraApi.getInstance().gWifiCamera.CallBackFucStart(WifiCallBackFuc.getInstance());
                }
            }.start();
            SystemClock.sleep(100L);
        }
    }

    public static WifiCallBackFuc getInstance() {
        return conConfigHolder.gWifiCallBackFuc;
    }
}
