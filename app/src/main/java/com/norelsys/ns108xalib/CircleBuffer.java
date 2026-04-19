package com.norelsys.ns108xalib;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CircleBuffer {
    int bufferNeedAfterFlow;
    private byte[] mBuffer;
    private int mCapacity;
    private int mReadPosition;
    private int mReadableCount;
    private int mWritePosition;
    final int WAIT_INTERRUPT_ERR = -1;
    final int DATA_OVERFLOWR_ERR = -2;
    final int WAIT_TIMEOUT_ERR = -3;
    final int MAX_PACKET_SIZE = 16384;
    final int BUFFER_NEED_AFTER_FLOW = 393216;
    boolean waitData = false;
    boolean pauseRevcData = false;
    private final long WAIT_TIMEOUT_SEC = 5;
    ReentrantLock lock = new ReentrantLock();
    Condition notEmptyCondition = this.lock.newCondition();
    Condition notFullCondition = this.lock.newCondition();

    public CircleBuffer(int i) {
        this.mBuffer = new byte[i];
        this.mCapacity = i;
        this.bufferNeedAfterFlow = Math.min(393216, this.mCapacity / 4);
        reset();
    }

    public int readBytes(byte[] bArr, int i) {
        this.lock.lock();
        while (this.mReadableCount < i) {
            try {
                this.waitData = true;
                if (!this.notEmptyCondition.await(5L, TimeUnit.SECONDS)) {
                    this.waitData = false;
                    this.lock.unlock();
                    return -3;
                }
            } catch (InterruptedException unused) {
                this.waitData = false;
                this.lock.unlock();
                return -1;
            }
        }
        int i2 = this.mReadPosition;
        int i3 = i2 + i;
        int i4 = this.mCapacity;
        if (i3 < i4) {
            System.arraycopy(this.mBuffer, i2, bArr, 0, i);
            this.mReadPosition += i;
        } else {
            int i5 = i4 - i2;
            int i6 = i - i5;
            System.arraycopy(this.mBuffer, i2, bArr, 0, i5);
            System.arraycopy(this.mBuffer, 0, bArr, i5, i6);
            this.mReadPosition = i6;
        }
        this.mReadableCount -= i;
        if (this.pauseRevcData && this.mCapacity - this.mReadableCount >= this.bufferNeedAfterFlow) {
            this.pauseRevcData = false;
            this.notFullCondition.signalAll();
        }
        this.lock.unlock();
        return i;
    }

    public void writeBytes(byte[] bArr) throws InterruptedException {
        this.lock.lock();
        if (this.mReadableCount + 16384 > this.mCapacity) {
            this.pauseRevcData = true;
            this.notFullCondition.await();
        }
        System.arraycopy(bArr, 0, this.mBuffer, this.mWritePosition, 16384);
        this.mReadableCount += 16384;
        if (this.waitData) {
            this.waitData = false;
            this.notEmptyCondition.signalAll();
        }
        this.lock.unlock();
        this.mWritePosition += 16384;
        int i = this.mWritePosition;
        int i2 = this.mCapacity;
        if (i >= i2) {
            this.mWritePosition = i - i2;
        }
    }

    public void reset() {
        this.mReadableCount = 0;
        this.mReadPosition = 0;
        this.mWritePosition = 0;
    }

    public String printHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            stringBuffer.append(hexString + " ");
        }
        return stringBuffer.toString();
    }

    public String printHexString(byte[] bArr, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            String hexString = Integer.toHexString(bArr[i2] & 255);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            stringBuffer.append(hexString + " ");
        }
        return stringBuffer.toString();
    }

    public byte[] buffer() {
        return this.mBuffer;
    }

    public int capacity() {
        return this.mCapacity;
    }

    public int readable() {
        return this.mReadableCount;
    }

    public boolean isEmpty() {
        return this.pauseRevcData;
    }
}
