package com.norelsys.ns108xalib;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Controller {
    CircleBuffer circleBuffer;
    FileInputStream fin;
    boolean closeCamera = true;
    CountDownLatch stopCapLatch = new CountDownLatch(1);
    boolean cameraRunning = false;

    public void setFin(FileInputStream fileInputStream) {
        this.fin = fileInputStream;
    }

    public Controller(CircleBuffer circleBuffer, FileInputStream fileInputStream) {
        this.circleBuffer = circleBuffer;
        this.fin = fileInputStream;
    }

    public Controller() {
    }

    public void setCircleBuffer(CircleBuffer circleBuffer) {
        this.circleBuffer = circleBuffer;
    }

    public void setReader(FileInputStream fileInputStream) {
        this.fin = fileInputStream;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.stopCapLatch = countDownLatch;
    }

    public void startCapture() {
        this.closeCamera = false;
        this.cameraRunning = true;
        new Thread(new CameraCapatureThread()).start();
    }

    public boolean stopCapture() throws InterruptedException {
        if (this.closeCamera) {
            return false;
        }
        this.closeCamera = true;
        this.stopCapLatch.await();
        new Thread(new ReadRemainDataThread()).start();
        return true;
    }

    public int uvcReceive(byte[] bArr, int i) {
        return this.circleBuffer.readBytes(bArr, i);
    }

    class ReadRemainDataThread implements Runnable {
        ReadRemainDataThread() {
        }

        @Override
        public void run() {
            byte[] bArr = new byte[16384];
            try {
                try {
                    int iAvailable = Controller.this.fin.available();
                    while (iAvailable > 0) {
                        if (iAvailable > 16384) {
                            iAvailable = 16384;
                        }
                        Controller.this.fin.read(bArr, 0, iAvailable);
                        iAvailable = Controller.this.fin.available();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                Controller.this.cameraRunning = false;
            }
        }
    }

    class CameraCapatureThread implements Runnable {
        int xferLen = 65536;
        byte[] buff = new byte[16384];

        CameraCapatureThread() {
        }

        @Override
        public void run() {
            while (!Controller.this.closeCamera) {
                int i = this.xferLen;
                while (i > 0) {
                    try {
                        int i2 = Controller.this.fin.read(this.buff);
                        try {
                            Controller.this.circleBuffer.writeBytes(this.buff);
                            i -= i2;
                        } catch (InterruptedException unused) {
                            Controller.this.closeCamera = true;
                        }
                    } catch (IOException unused2) {
                        Controller controller = Controller.this;
                        controller.closeCamera = true;
                        controller.cameraRunning = false;
                    }
                }
            }
            Controller.this.stopCapLatch.countDown();
        }
    }

    public boolean isCameraRunning() {
        return this.cameraRunning;
    }
}
