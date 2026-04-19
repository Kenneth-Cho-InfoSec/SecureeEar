package com.i4season.bkCamera.logicrelated.database;

import com.i4season.bkCamera.logicrelated.database.bandingdevice.BindingDeviceDataBaseManager;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBaseManager {
    private static DataBaseManager instance;
    private static Lock mLock = new ReentrantLock();
    private BindingDeviceDataBaseManager mBindingDeviceDataBaseManager = new BindingDeviceDataBaseManager();

    public static DataBaseManager getInstance() {
        if (instance == null) {
            mLock.lock();
            if (instance == null) {
                instance = new DataBaseManager();
            }
            mLock.unlock();
        }
        return instance;
    }

    private DataBaseManager() {
    }

    public BindingDeviceDataBaseManager getmBindingDeviceDataBaseManager() {
        return this.mBindingDeviceDataBaseManager;
    }
}
