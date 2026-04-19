package com.jni.Tieniu;

public class TieniuInfo {
    public int CAMCAP_1080 = 0;
    public int CAMCAP_720 = 1;
    public int CAMCAP_WVGA = 2;
    public int CAMCAP_VGA = 3;
    int resolution = 0;
    int Contrast = 0;
    int Sharpness = 0;
    int Brightness = 0;

    int f78R = 0;

    int f77G = 0;

    int f76B = 0;

    public void setResolution(int i) {
        this.resolution = i;
    }

    public void setContrast(int i) {
        this.Contrast = i;
    }

    public void setSharpness(int i) {
        this.Sharpness = i;
    }

    public void setBrightness(int i) {
        this.Brightness = i;
    }

    public void setR(int i) {
        this.f78R = i;
    }

    public void setG(int i) {
        this.f77G = i;
    }

    public void setB(int i) {
        this.f76B = i;
    }
}
