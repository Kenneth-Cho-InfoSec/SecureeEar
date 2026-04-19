package com.i4season.bkCamera.logicrelated.fileacceptandoperation.datasource.explorer.localfilemanage;

import android.os.Parcel;
import android.os.Parcelable;

public class MountDirNew implements Parcelable {
    public static final Parcelable.Creator<MountDirNew> CREATOR = new Parcelable.Creator<MountDirNew>() {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override
        public MountDirNew createFromParcel(Parcel parcel) {
            return new MountDirNew(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override
        public MountDirNew[] newArray(int i) {
            return new MountDirNew[i];
        }
    };
    private boolean isRemovale;
    private String path;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.path);
        parcel.writeByte(this.isRemovale ? (byte) 1 : (byte) 0);
    }

    public MountDirNew() {
        this.path = null;
        this.isRemovale = false;
    }

    protected MountDirNew(Parcel parcel) {
        this.path = null;
        this.isRemovale = false;
        this.path = parcel.readString();
        this.isRemovale = parcel.readByte() != 0;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public boolean isRemovale() {
        return this.isRemovale;
    }

    public void setIsRemovale(boolean z) {
        this.isRemovale = z;
    }
}
