package com.example.xue.myqq.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * QQ用户信息 实现Parceable 后面需要通过Intent去传
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/23 12:18
 **/

@Data
@Accessors(chain = true)
public class FriendInfo implements Parcelable {
    private int id;
    private Bitmap portrait;
    private String nickname;
    private String remark;
    private String account;
    private String sex;
    private String age;
    private String sign;

    public FriendInfo() {
    }

    protected FriendInfo(Parcel in) {
        id = in.readInt();
        portrait = in.readParcelable(Bitmap.class.getClassLoader());
        nickname = in.readString();
        remark = in.readString();
        account = in.readString();
        sex = in.readString();
        age = in.readString();
        sign = in.readString();
    }

    public static final Creator<FriendInfo> CREATOR = new Creator<FriendInfo>() {
        @Override
        public FriendInfo createFromParcel(Parcel in) {
            return new FriendInfo(in);
        }

        @Override
        public FriendInfo[] newArray(int size) {
            return new FriendInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(portrait, flags);
        dest.writeString(nickname);
        dest.writeString(remark);
        dest.writeString(account);
        dest.writeString(sex);
        dest.writeString(age);
        dest.writeString(sign);
    }
}
