package com.example.xue.myqq.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.xue.myqq.R;
import com.example.xue.myqq.util.UserUtil;

public class UserInfoActivity extends AppCompatActivity {

    private TextView mUserInfoAccountTextView;
    private TextView mUserInfoSexTextView;
    private TextView mUserInfoPhoneTextView;
    private TextView mUserInfoAddressTextView;
    private ContentResolver mContentResolver;
    private String mSex;
    private String mPhone;
    private String mAddress;
    private static final String TAG = "UserInfoActivity";
    private String mUserAccountMain;
    private TextView mUserInfoNikenameTextView;
    private String mNikename;


//    @Override
//    protected void onCreate( Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        Log.d(TAG, "onCreate: 进入了UserInfoActivity");
        init();
        initEvent();
    }
    public void init() {
        mUserInfoAccountTextView = findViewById(R.id.tv_userinfo_accout_id);
        mUserInfoSexTextView = findViewById(R.id.tv_userinfo_sex_id);
        mUserInfoPhoneTextView = findViewById(R.id.tv_userinfo_phone_id);
        mUserInfoAddressTextView = findViewById(R.id.tv_userinfo_address_id);
        mUserInfoNikenameTextView = findViewById(R.id.tv_userinfo_nickname_id);

        mContentResolver = getContentResolver();
//        Intent intent = getIntent();
//        mUserAccountMain = intent.getStringExtra("userAccountMain");
    }

    public void initEvent() {
        mUserAccountMain = getAccountFromSP();
        Cursor cursor = mContentResolver.query(Uri.parse(UserUtil.USERURI), null, "account = ?", new String[]{mUserAccountMain}, null);
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                cursor.moveToFirst();
                mSex = cursor.getString(cursor.getColumnIndex("sex"));
                mPhone = cursor.getString(cursor.getColumnIndex("phone"));
                mAddress = cursor.getString(cursor.getColumnIndex("address"));
                mNikename = cursor.getString(cursor.getColumnIndex("nikename"));

            }
        }

        switch (mSex){
            case "0":{
                mUserInfoSexTextView.setText("性别: 男");
            break;
            }
            case "1":{
                mUserInfoSexTextView.setText("性别: 女");
                break;
            }
        }
        mUserInfoNikenameTextView.setText("昵称: "+mNikename);
        mUserInfoAccountTextView.setText("账号: "+mUserAccountMain);
        mUserInfoPhoneTextView.setText("手机号: "+mPhone);
        mUserInfoAddressTextView.setText("家庭地址: "+mAddress);
    }
    private String getAccountFromSP(){
        SharedPreferences sp = getSharedPreferences("account", Context.MODE_PRIVATE);
        String account = sp.getString("accountId", "wrong");
        return account;
    }
}
