package com.example.xue.myqq.bean;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;


public class PictureViewInfo implements Parcelable {

    private String url;
    private int index;
    private Rect mBounds;

    public PictureViewInfo(String url, int index) {
        this.url = url;
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Rect getBounds() {
        return mBounds;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeInt(this.index);
        dest.writeParcelable(this.mBounds, flags);
    }

    private PictureViewInfo(Parcel in) {
        this.url = in.readString();
        this.index = in.readInt();
        this.mBounds = in.readParcelable(Rect.class.getClassLoader());
    }

    public static final Creator<PictureViewInfo> CREATOR = new Creator<PictureViewInfo>() {
        @Override
        public PictureViewInfo createFromParcel(Parcel source) {
            return new PictureViewInfo(source);
        }

        @Override
        public PictureViewInfo[] newArray(int size) {
            return new PictureViewInfo[size];
        }
    };
}
