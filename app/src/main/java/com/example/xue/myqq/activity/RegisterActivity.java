package com.example.xue.myqq;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.xue.myqq.bean.UserInfo;
import com.example.xue.myqq.util.MD5;
import com.example.xue.myqq.util.UserUtil;

public class RegisterActivity extends AppCompatActivity {

    private EditText mRegisterAccountEditText;
    private EditText mRegisterPasswordEditText;
    private EditText mRegisterPasswordAgainEditText;
    private EditText mRegisterPhoneEditText;
    private EditText mRegisterAddressEditText;
    private CheckBox mRegisterAgreementCheckBox;
    private Button mRegisterButton;
    private ContentResolver mContentResolver;
    private UserInfo mUserInfo;
    public final static int RESULTCODE = 5;
    private RadioGroup mSexRadio;
    private Uri mUserInfoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContentResolver = getContentResolver();
        mUserInfoUri = Uri.parse(UserUtil.USERURI);
        mUserInfo = new UserInfo();
        initView();
        clickEvent();

    }

    /**
     * 初始化各种组件
     */
    public void initView() {
        mRegisterAccountEditText = findViewById(R.id.et_register_name_id);
        mRegisterPasswordEditText = findViewById(R.id.et_register_password_id);
        mRegisterPasswordAgainEditText = findViewById(R.id.et_register_psw_again_id);
        mRegisterPhoneEditText = findViewById(R.id.et_register_phone_id);
        mRegisterAddressEditText = findViewById(R.id.et_register_address_id);
        mSexRadio = findViewById(R.id.SexRadio_id);
        mRegisterAgreementCheckBox = findViewById(R.id.cb_register_agreement_id);
        mRegisterButton = findViewById(R.id.bt_register_id);
    }

    /**
     * 点击事件，主要是注册
     */
    public void clickEvent() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditStr();

                if (TextUtils.isEmpty(mUserInfo.getAccount())) {
                    mRegisterAccountEditText.setError("账号不能为空");
                } else if (TextUtils.isEmpty(mUserInfo.getPassword())) {
                    mRegisterPasswordEditText.setError("密码不能为空");
                } else if (TextUtils.isEmpty(mUserInfo.getPswagain())) {
                    mRegisterPasswordAgainEditText.setError("校验密码不能为空");
                } else if (TextUtils.isEmpty(mUserInfo.getPhone())) {
                    mRegisterPhoneEditText.setError("手机号不能为空");
                } else if (TextUtils.isEmpty(mUserInfo.getAddress())) {
                    mRegisterAddressEditText.setError("地址不能为空");
                } else if (mUserInfo.getSex() < 0) {
                    Toast.makeText(RegisterActivity.this, "请勾选您的性别", Toast.LENGTH_SHORT).show();

                } else if (!mUserInfo.getPassword().equals(mUserInfo.getPswagain())) {
                    Toast.makeText(RegisterActivity.this, "两次密码要一致", Toast.LENGTH_SHORT).show();
                } else if (!mRegisterAgreementCheckBox.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "请勾选已读协议", Toast.LENGTH_SHORT).show();
                } else if (isExistAccount()) {
                    Toast.makeText(RegisterActivity.this, "账户已经存在，请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    userInfoInsert();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("account", mUserInfo.getAccount());
                    setResult(5, intent);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();//返回登录页面
                }

            }
        });


    }

    /**
     * 获得相应控件中的数据，赋值到实体类
     */
    public void getEditStr() {
        String account = mRegisterAccountEditText.getText().toString().trim();
        String password = mRegisterPasswordEditText.getText().toString().trim();
        String pswagain = mRegisterPasswordAgainEditText.getText().toString().trim();
        String phone = mRegisterPhoneEditText.getText().toString().trim();
        String address = mRegisterAddressEditText.getText().toString().trim();
        int sex = -1;
        int checkedRadioButtonId = mSexRadio.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.rb_register_man_id: {
                sex = 0;
                break;
            }
            case R.id.rb_register_woman_id: {
                sex = 1;
                break;
            }
            default: {
                sex = -1;
                break;
            }
        }

        mUserInfo.setSex(sex);
        mUserInfo.setAccount(account);
        mUserInfo.setPassword(password);
        mUserInfo.setPswagain(pswagain);
        mUserInfo.setPhone(phone);
        mUserInfo.setAddress(address);
    }

    /**
     * 判断该账号是否已存在
     *
     * @return 返回true，表示userInfo中已存在该账号，返回false，反之。
     */
    private boolean isExistAccount() {
        Cursor cursor = mContentResolver.query(mUserInfoUri, null, "account = ? ", new String[]{mUserInfo.getAccount()}, null);
        return cursor.getCount() != 0;
    }

    /**
     * 将注册信息插入到数据库中
     */
    private void userInfoInsert() {
        ContentValues values = new ContentValues();
        values.put("account", mUserInfo.getAccount());
        //md5加密
        String password = MD5.getMD5(mUserInfo.getPassword());
        values.put("password", password);
        values.put("phone", mUserInfo.getPhone());
        values.put("address", mUserInfo.getAddress());
        values.put("Sex", mUserInfo.getSex());
        mContentResolver.insert(mUserInfoUri, values);
    }

}
