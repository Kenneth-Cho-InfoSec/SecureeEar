package com.jni.lic;

public class LicInfo {
    public String brand;
    public String factory;
    public int localcheckednum;
    public int localnum;
    public int localusednum;
    public int maxonetime;
    public String orderNumber;
    public int servernum;
    public int serverputnum;
    public int totalcheckd;
    public int totalnum;
    public int totalput;
    public int totalused;
    public String type;

    public void SetMaxonetime(int i) {
        this.maxonetime = i;
    }

    public void SetTotalnum(int i) {
        this.totalnum = i;
    }

    public void SetServernum(int i) {
        this.servernum = i;
    }

    public void SetServerputnum(int i) {
        this.serverputnum = i;
    }

    public void SetLocalnum(int i) {
        this.localnum = i;
    }

    public void SetLocalusednum(int i) {
        this.localusednum = i;
    }

    public void SetLocalcheckednum(int i) {
        this.localcheckednum = i;
    }

    public void SetTotalused(int i) {
        this.totalused = i;
    }

    public void SetTotalcheckd(int i) {
        this.totalcheckd = i;
    }

    public void SetTotalput(int i) {
        this.totalput = i;
    }

    public void SetOrderNumber(String str) {
        this.orderNumber = str;
    }

    public void SetFactory(String str) {
        this.factory = str;
    }

    public void SetBrand(String str) {
        this.brand = str;
    }

    public void SetType(String str) {
        this.type = str;
    }
}
