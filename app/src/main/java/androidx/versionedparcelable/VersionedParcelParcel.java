package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

class VersionedParcelParcel extends VersionedParcel {
    private static final boolean DEBUG = false;
    private static final String TAG = "VersionedParcelParcel";
    private int mCurrentField;
    private final int mEnd;
    private int mNextRead;
    private final int mOffset;
    private final Parcel mParcel;
    private final SparseIntArray mPositionLookup;
    private final String mPrefix;

    VersionedParcelParcel(Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "");
    }

    VersionedParcelParcel(Parcel parcel, int i, int i2, String str) {
        this.mPositionLookup = new SparseIntArray();
        this.mCurrentField = -1;
        this.mNextRead = 0;
        this.mParcel = parcel;
        this.mOffset = i;
        this.mEnd = i2;
        this.mNextRead = this.mOffset;
        this.mPrefix = str;
    }

    private int readUntilField(int i) {
        int i2;
        do {
            int i3 = this.mNextRead;
            if (i3 >= this.mEnd) {
                return -1;
            }
            this.mParcel.setDataPosition(i3);
            int i4 = this.mParcel.readInt();
            i2 = this.mParcel.readInt();
            this.mNextRead += i4;
        } while (i2 != i);
        return this.mParcel.dataPosition();
    }

    @Override
    public boolean readField(int i) {
        int untilField = readUntilField(i);
        if (untilField == -1) {
            return false;
        }
        this.mParcel.setDataPosition(untilField);
        return true;
    }

    @Override
    public void setOutputField(int i) {
        closeField();
        this.mCurrentField = i;
        this.mPositionLookup.put(i, this.mParcel.dataPosition());
        writeInt(0);
        writeInt(i);
    }

    @Override
    public void closeField() {
        int i = this.mCurrentField;
        if (i >= 0) {
            int i2 = this.mPositionLookup.get(i);
            int iDataPosition = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(i2);
            this.mParcel.writeInt(iDataPosition - i2);
            this.mParcel.setDataPosition(iDataPosition);
        }
    }

    @Override
    protected VersionedParcel createSubParcel() {
        Parcel parcel = this.mParcel;
        int iDataPosition = parcel.dataPosition();
        int i = this.mNextRead;
        if (i == this.mOffset) {
            i = this.mEnd;
        }
        return new VersionedParcelParcel(parcel, iDataPosition, i, this.mPrefix + "  ");
    }

    @Override
    public void writeByteArray(byte[] bArr) {
        if (bArr != null) {
            this.mParcel.writeInt(bArr.length);
            this.mParcel.writeByteArray(bArr);
        } else {
            this.mParcel.writeInt(-1);
        }
    }

    @Override
    public void writeByteArray(byte[] bArr, int i, int i2) {
        if (bArr != null) {
            this.mParcel.writeInt(bArr.length);
            this.mParcel.writeByteArray(bArr, i, i2);
        } else {
            this.mParcel.writeInt(-1);
        }
    }

    @Override
    public void writeInt(int i) {
        this.mParcel.writeInt(i);
    }

    @Override
    public void writeLong(long j) {
        this.mParcel.writeLong(j);
    }

    @Override
    public void writeFloat(float f) {
        this.mParcel.writeFloat(f);
    }

    @Override
    public void writeDouble(double d) {
        this.mParcel.writeDouble(d);
    }

    @Override
    public void writeString(String str) {
        this.mParcel.writeString(str);
    }

    @Override
    public void writeStrongBinder(IBinder iBinder) {
        this.mParcel.writeStrongBinder(iBinder);
    }

    @Override
    public void writeParcelable(Parcelable parcelable) {
        this.mParcel.writeParcelable(parcelable, 0);
    }

    @Override
    public void writeBoolean(boolean z) {
        this.mParcel.writeInt(z ? 1 : 0);
    }

    @Override
    public void writeStrongInterface(IInterface iInterface) {
        this.mParcel.writeStrongInterface(iInterface);
    }

    @Override
    public void writeBundle(Bundle bundle) {
        this.mParcel.writeBundle(bundle);
    }

    @Override
    public int readInt() {
        return this.mParcel.readInt();
    }

    @Override
    public long readLong() {
        return this.mParcel.readLong();
    }

    @Override
    public float readFloat() {
        return this.mParcel.readFloat();
    }

    @Override
    public double readDouble() {
        return this.mParcel.readDouble();
    }

    @Override
    public String readString() {
        return this.mParcel.readString();
    }

    @Override
    public IBinder readStrongBinder() {
        return this.mParcel.readStrongBinder();
    }

    @Override
    public byte[] readByteArray() {
        int i = this.mParcel.readInt();
        if (i < 0) {
            return null;
        }
        byte[] bArr = new byte[i];
        this.mParcel.readByteArray(bArr);
        return bArr;
    }

    @Override
    public <T extends Parcelable> T readParcelable() {
        return (T) this.mParcel.readParcelable(getClass().getClassLoader());
    }

    @Override
    public Bundle readBundle() {
        return this.mParcel.readBundle(getClass().getClassLoader());
    }

    @Override
    public boolean readBoolean() {
        return this.mParcel.readInt() != 0;
    }
}
