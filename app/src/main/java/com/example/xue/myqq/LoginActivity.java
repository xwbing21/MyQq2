package com.example.xue.myqq;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xue.myqq.bean.UserInfo;

public class LoginActivity extends AppCompatActivity {

    private EditText mEt_login_account;
    private EditText mEt_login_psw;
    private Button mBtn_register;
    private TextView mTv_login_noregister;
//    private TextView mTv_login_forgetpsw;
    private UserInfo userInfo;
    private ContentResolver mContentResolver;
    public final static String USERURI = "content://com.example.xue.myqq.UserContentProviderprovider/user";
    private Uri mUserUri;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContentResolver = getContentResolver();
        mUserUri = Uri.parse(USERURI);
        userInfo = new UserInfo();
        initView();
        init();

    }
    private void initView(){
        mEt_login_account = findViewById(R.id.et_login_account);
        mEt_login_psw = findViewById(R.id.et_login_psw);
        mBtn_register = findViewById(R.id.bt_login_login);
        mTv_login_noregister = findViewById(R.id.tv_login_noregister);
//        mTv_login_forgetpsw = findViewById(R.id.tv_login_forgetpsw);

    }
    public void init(){
        mBtn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditStr();
                Log.d(TAG, "onClick: mBtn_register:"+ userInfo.getAccount());
                if(TextUtils.isEmpty(userInfo.getAccount())){
                    mEt_login_account.setError("账号不能为空");
                }else if (TextUtils.isEmpty(userInfo.getPassword())){
                    mEt_login_psw.setError("密码不能为空");
                }else {
                    Cursor cursor = mContentResolver.query(mUserUri, null, "account =? and password=?", new String[]{userInfo.getAccount(),userInfo.getPassword()}, null);
                    if (cursor.getCount()>0){

//                        String dbPassword = cursor.getString(cursor.getColumnIndex("password"));
//                        Log.d(TAG, "init:dbPassword "+dbPassword);
//                        if (!dbPassword.equals(userInfo.getAccount())){
//                            Toast.makeText(LoginActivity.this,"密码不正确",Toast.LENGTH_SHORT).show();
//                        }else {
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
//                        }

                    }else {
                        Toast.makeText(LoginActivity.this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        mTv_login_noregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 5){
            String account = data.getStringExtra("account");
            if(!TextUtils.isEmpty(account)){
                mEt_login_account.setText(account);
                mEt_login_account.setSelection(account.length());
            }
        }
    }
    private void getEditStr(){
        String account = mEt_login_account.getText().toString().trim();
        Log.d(TAG, "initView: account"+account);
        String password = mEt_login_psw.getText().toString().trim();
        userInfo.setAccount(account);
        userInfo.setPassword(password);
    }
}
