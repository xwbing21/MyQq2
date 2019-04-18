package com.example.xue.myqq;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.xue.myqq.bean.UserInfo;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEt_register_name;
    private EditText mEt_register_password;
    private EditText mEt_register_psw_again;
    private EditText mEt_register_phone;
    private EditText mEt_register_address;
    private RadioButton mRb_register_man;
    private RadioButton mRb_register_woman;
    private CheckBox mCb_register_agreement;
    private Button mBt_register;
    private ContentResolver mContentResolver;
    private UserInfo userInfo;
    public final static String USERURI = "content://com.example.xue.myqq.UserContentProviderprovider/user";
    public final static int RESULTCODE = 5;
    private RadioGroup mSexRadio;
    private Uri userInfoUri;
    private CheckBox mCb_register_agreement1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContentResolver = getContentResolver();
        userInfoUri = Uri.parse(USERURI);
        userInfo = new UserInfo();
        initView();
        init();

    }

    public void initView() {
        mEt_register_name = findViewById(R.id.et_register_name);
        mEt_register_password = findViewById(R.id.et_register_password);
        mEt_register_psw_again = findViewById(R.id.et_register_psw_again);
        mEt_register_phone = findViewById(R.id.et_register_phone);
        mEt_register_address = findViewById(R.id.et_register_address);
        mSexRadio = findViewById(R.id.SexRadio);
        mRb_register_man = findViewById(R.id.rb_register_man);
        mRb_register_woman = findViewById(R.id.rb_register_woman);
        mCb_register_agreement = findViewById(R.id.cb_register_agreement);
        mBt_register = findViewById(R.id.btn_register);
        mCb_register_agreement1 = findViewById(R.id.cb_register_agreement);
    }

    public void init() {
        mBt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditStr();

                if (TextUtils.isEmpty(userInfo.getAccount())) {
                    mEt_register_name.setError("账号不能为空");
                } else if (TextUtils.isEmpty(userInfo.getPassword())) {
                    mEt_register_password.setError("密码不能为空");
                } else if (TextUtils.isEmpty(userInfo.getPassword())) {
                    mEt_register_psw_again.setError("校验密码不能为空");
                } else if (TextUtils.isEmpty(userInfo.getPassword())) {
                    mEt_register_phone.setError("手机号不能为空");
                } else if (TextUtils.isEmpty(userInfo.getPassword())) {
                    mEt_register_address.setError("地址不能为空");
                } else if (userInfo.getSex() < 0) {
                    Toast.makeText(RegisterActivity.this, "请勾选您的性别", Toast.LENGTH_SHORT).show();

                } else if (!userInfo.getPassword().equals(userInfo.getPswagain())) {
                    Toast.makeText(RegisterActivity.this, "两次密码要一致", Toast.LENGTH_SHORT).show();
                } else if (!mCb_register_agreement1.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "请勾选已读协议", Toast.LENGTH_SHORT).show();
                } else if (isExist()) {
                    Toast.makeText(RegisterActivity.this, "账户已经存在，请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    userInfoInsert();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("account", userInfo.getAccount());
                    setResult(RESULTCODE, intent);
                    RegisterActivity.this.finish();//返回登录页面
                }

            }
        });




    }

    public void getEditStr() {
        String account = mEt_register_name.getText().toString().trim();
        String password = mEt_register_password.getText().toString().trim();
        String pswagain = mEt_register_psw_again.getText().toString().trim();
        String phone = mEt_register_phone.getText().toString().trim();
        String address = mEt_register_address.getText().toString().trim();
        int sex = -1;
        int checkedRadioButtonId = mSexRadio.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.rb_register_man: {
                sex = 0;
                break;
            }
            case R.id.rb_register_woman: {
                sex = 1;
                break;
            }
            default: {
                sex = -1;
                break;
            }
        }

        userInfo.setSex(sex);
        userInfo.setAccount(account);
        userInfo.setPassword(password);
        userInfo.setPswagain(pswagain);
        userInfo.setPhone(phone);
        userInfo.setAddress(address);
    }


    private boolean isExist() {
        Cursor cursor = mContentResolver.query(userInfoUri, null, "account = ? ", new String[]{userInfo.getAccount()}, null);
        return cursor.getCount() != 0;
    }

    private void userInfoInsert() {
        ContentValues values = new ContentValues();
        values.put("account", userInfo.getAccount());
        values.put("password", userInfo.getPassword());
        values.put("phone", userInfo.getPhone());
        values.put("address", userInfo.getAddress());
        values.put("Sex", userInfo.getSex());
        mContentResolver.insert(userInfoUri, values);
    }

}
