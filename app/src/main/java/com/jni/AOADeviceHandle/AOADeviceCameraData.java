package com.jni.AOADeviceHandle;

public class AOADeviceCameraData {
    public byte[] data = null;
    public int type = 0;
    public int take = 0;
    public int zoom = 0;
    public int electricity = 0;
    public int changeCamera = 0;
    public int poweroff = 0;
    public int charging = 0;
    public float angle = 0.0f;
    public float battery = 0.0f;
    public int close = 0;
    public int mode = 0;
    public int motor = 0;
    public int notice = 0;
    public int needmove = 0;
    public int camera_motor_change = 0;
    public int motor_level = 0;
    public int is_timeoutpoweroff = 0;
    public int motor_value = 0;
    public byte[] yuvdata = null;
    public int yuvheight = 0;
    public int yuvwidth = 0;

    public byte[] getData() {
        return this.data;
    }

    public void SetData(byte[] bArr) {
        this.data = bArr;
    }

    public void SetType(int i) {
        this.type = i;
    }

    public int GetType() {
        return this.type;
    }

    public void SetTake(int i) {
        this.take = i;
    }

    public int GetTake() {
        return this.take;
    }

    public void SetZoom(int i) {
        this.zoom = i;
    }

    public int GetZoom() {
        return this.zoom;
    }

    public void Setelectricity(int i) {
        this.electricity = i;
    }

    public int Getelectricity() {
        return this.electricity;
    }

    public void SetchangeCamera(int i) {
        this.changeCamera = i;
    }

    public int GetchangeCamera() {
        return this.changeCamera;
    }

    public void Setpoweroff(int i) {
        this.poweroff = i;
    }

    public int Getpoweroff() {
        return this.poweroff;
    }

    public void Setcharging(int i) {
        this.charging = i;
    }

    public int Getcharging() {
        return this.charging;
    }

    public void Setangle(float f) {
        this.angle = f;
    }

    public float Getangle() {
        return this.angle;
    }

    public void Setbattery(float f) {
        this.battery = f;
    }

    public float Getbattery() {
        return this.battery;
    }

    public void Setclose(int i) {
        this.close = i;
    }

    public int Getclose() {
        return this.close;
    }

    public void Setmode(int i) {
        this.mode = i;
    }

    public int Getmode() {
        return this.mode;
    }

    public void Setmotor(int i) {
        this.motor = i;
    }

    public int Getmotor() {
        return this.motor;
    }

    public void Setnotice(int i) {
        this.notice = i;
    }

    public int Getnotice() {
        return this.notice;
    }

    public void Setneedmove(int i) {
        this.needmove = i;
    }

    public int Getneedmove() {
        return this.needmove;
    }

    public void Setcamera_motor_change(int i) {
        this.camera_motor_change = i;
    }

    public int Getcamera_motor_change() {
        return this.camera_motor_change;
    }

    public void Setmotor_level(int i) {
        this.motor_level = i;
    }

    public void Setis_timeoutpoweroff(int i) {
        this.is_timeoutpoweroff = i;
    }

    public void SetmyuvData(byte[] bArr) {
        this.yuvdata = bArr;
    }

    public void Setyuvheight(int i) {
        this.yuvheight = i;
    }

    public void Setyuvwidth(int i) {
        this.yuvwidth = i;
    }

    public void Setmotor_value(int i) {
        this.motor_value = i;
    }
}
