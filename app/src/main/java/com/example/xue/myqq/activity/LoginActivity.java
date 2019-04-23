package com.example.xue.myqq.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.xue.myqq.R;
import com.example.xue.myqq.bean.UserInfo;
import com.example.xue.myqq.util.MD5;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginAccountEditText;
    private EditText mLoginPasswordEditText;
    private Button mLoginButton;
    private TextView mUnRegisterTextView;
    private UserInfo mUserInfo;
    private ContentResolver mContentResolver;
    public final static String USERURI = "content://com.example.xue.myqq.UserContentProviderprovider/user";
    private Uri mUserUri;
    private static final String TAG = "LoginActivity";
    private TextView mResetPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置在加载布局之前,如果loginExist为true，就直接跳转主页面
        SharedPreferences sp = getSharedPreferences("loginState", Context.MODE_PRIVATE);
        boolean loginState = sp.getBoolean("loginExist", false);
        if (loginState) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

        setContentView(R.layout.activity_login);
        mContentResolver = getContentResolver();
        mUserUri = Uri.parse(USERURI);
        mUserInfo = new UserInfo();
        initView();
        clickEvent();

    }

    /**
     * 初始化各组件
     */
    private void initView() {
        mLoginAccountEditText = findViewById(R.id.et_login_account_id);
        mLoginPasswordEditText = findViewById(R.id.et_login_psw_id);
        mLoginButton = findViewById(R.id.bt_login_login_id);
        mUnRegisterTextView = findViewById(R.id.tv_login_unregister_id);
        mResetPasswordTextView = findViewById(R.id.tv_login_resetpsw);
    }

    /**
     * login界面点击事件
     */
    public void clickEvent() {

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditStr();
                Log.d(TAG, "onClick: mLoginButton:" + mUserInfo.getAccount());
                if (TextUtils.isEmpty(mUserInfo.getAccount())) {
                    mLoginAccountEditText.setError("账号不能为空");
                } else if (TextUtils.isEmpty(mUserInfo.getPassword())) {
                    mLoginPasswordEditText.setError("密码不能为空");
                } else {
                    //因为密码md5加密过后的，所以此部分也要加密后比对数据库存的值
                    String md5Password = MD5.getMD5(mUserInfo.getPassword());
                    Cursor cursor = mContentResolver.query(mUserUri, null, "account =? and password=?", new String[]{mUserInfo.getAccount(), md5Password}, null);
                    if (cursor.getCount() > 0) {
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                        setLoginState();
                        setUserAccount();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码不正确" + mUserInfo.getPassword(), Toast.LENGTH_SHORT).show();
                    }

                }
            }


        });
        mUnRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        mResetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditStr();
                Intent intent = new Intent(LoginActivity.this, ResetActivity.class);
                intent.putExtra("accountFromLogin", mUserInfo.getAccount());
                startActivityForResult(intent, 2);
                Log.d(TAG, "onClick: mUserInfo.getAccount=" + mUserInfo.getAccount());
            }
        });

    }

    /**
     * 从注册页面注册完账号，返回注册的用户名，进行用户名回显
     *
     * @param requestCode
     * @param resultCode
     * @param data        这里传回的是注册页面的数据（account）
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5) {
            String account = data.getStringExtra("account");
            if (!TextUtils.isEmpty(account)) {
                mLoginAccountEditText.setText(account);
                mLoginAccountEditText.setSelection(account.length());
                Log.d(TAG, "onActivityResult: account" + account);
            }
        } else if (resultCode == 2) {
            String accountFromReset = data.getStringExtra("accountFromReset");
            if (!TextUtils.isEmpty(accountFromReset)) {
                mLoginAccountEditText.setText(accountFromReset);
                mLoginAccountEditText.setSelection(accountFromReset.length());
                Log.d(TAG, "onActivityResult: account" + accountFromReset);
            }
        }
    }

    /**
     * 获得相应控件中的数据，赋值到实体类中
     */
    private void getEditStr() {
        String account = mLoginAccountEditText.getText().toString().trim();
        Log.d(TAG, "initView: account" + account);
        String password = mLoginPasswordEditText.getText().toString().trim();
        mUserInfo.setAccount(account);
        mUserInfo.setPassword(password);
    }

    /**
     * 设置保存登录状态，保存在sp中
     */
    private void setLoginState() {
        SharedPreferences sp = getSharedPreferences("loginState", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("loginExist", true);
        edit.commit();
    }

    /**
     * 设置保存账号，保存在sp中
     */
    private void setUserAccount() {
        SharedPreferences spAccount = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spAccount.edit();
        edit.putString("accountId", mUserInfo.getAccount());
        edit.commit();
    }

}
