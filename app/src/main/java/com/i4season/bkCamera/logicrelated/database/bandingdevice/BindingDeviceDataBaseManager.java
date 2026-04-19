package com.i4season.bkCamera.logicrelated.database.bandingdevice;

import com.i4season.bkCamera.uirelated.functionpage.homepage.bean.UserDeviceInfoBean;
import java.util.List;

public class BindingDeviceDataBaseManager {
    private BindingDeviceOpt mBindingDeviceOpt = new BindingDeviceOpt();

    public void saveBindingDeviceInfo(UserDeviceInfoBean userDeviceInfoBean) {
        BindingDeviceOpt bindingDeviceOpt = this.mBindingDeviceOpt;
        if (bindingDeviceOpt != null) {
            bindingDeviceOpt.saveBindingDeviceInfo(userDeviceInfoBean);
        }
    }

    public void updateBindDeviceInfoRecord(UserDeviceInfoBean userDeviceInfoBean) {
        BindingDeviceOpt bindingDeviceOpt = this.mBindingDeviceOpt;
        if (bindingDeviceOpt != null) {
            bindingDeviceOpt.updateBindDeviceInfoRecord(userDeviceInfoBean);
        }
    }

    public List<UserDeviceInfoBean> acceptAllBindingDevice() {
        BindingDeviceOpt bindingDeviceOpt = this.mBindingDeviceOpt;
        if (bindingDeviceOpt != null) {
            return bindingDeviceOpt.acceptAllBindingDevice();
        }
        return null;
    }

    public UserDeviceInfoBean acceptBindDeviceInfoFromSsid(String str) {
        BindingDeviceOpt bindingDeviceOpt = this.mBindingDeviceOpt;
        if (bindingDeviceOpt != null) {
            return bindingDeviceOpt.acceptBindDeviceInfoFromSsid(str);
        }
        return null;
    }

    public boolean deleteDevUserInfoFromUserId(String str) {
        BindingDeviceOpt bindingDeviceOpt = this.mBindingDeviceOpt;
        if (bindingDeviceOpt != null) {
            return bindingDeviceOpt.deleteBindingDeviceInfoRecordFromDevId(str);
        }
        return false;
    }
}
