package com.example.xue.myqq.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xue.myqq.R;
import com.example.xue.myqq.util.MD5;
import com.example.xue.myqq.util.UserUtil;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText mResetAccountEditText;
    private EditText mResetOldpswEditText;
    private Button mResetButton;
    private static final String TAG = "ResetActivity";
    private ContentResolver mContentResolver;
    private EditText mResetNewpswEditText;
    private String mAccount;
    private String mOldPasword;
    private String mNewPassword;
    private String mAccountFromLogin;
    private String mLastAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        init();
    }

    private void init() {
        mResetAccountEditText = findViewById(R.id.et_reset_account_id);
        mResetOldpswEditText = findViewById(R.id.et_reset_oldpsw_id);
        mResetNewpswEditText = findViewById(R.id.et_reset_newpsw_id);
        mResetButton = findViewById(R.id.bt_reset_reset_id);
        mResetButton.setOnClickListener(this);
        Intent intent = getIntent();
        mAccountFromLogin = intent.getStringExtra("accountFromLogin");
        Log.d(TAG, "onCreate: mAccountFromLogin" + mAccountFromLogin);
        if (mAccountFromLogin!=null){
            mResetAccountEditText.setText(mAccountFromLogin);
        }
    }

    /**
     * ResetActivity的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        mLastAccount = null;
        if (TextUtils.isEmpty(mAccountFromLogin)) {
            mAccount = mResetAccountEditText.getText().toString().trim();
            mLastAccount = mAccount;
        } else {
            mLastAccount = mAccountFromLogin;
        }
        mOldPasword = mResetOldpswEditText.getText().toString().trim();
        mNewPassword = mResetNewpswEditText.getText().toString().trim();

        mContentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        if (v.getId() == R.id.bt_reset_reset_id) {
            String md5Password = MD5.getMD5(mNewPassword);
            values.put("password", md5Password);
            if (!isExistAccount()) {
                Toast.makeText(ResetActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
            } else if (!isCorrectPassword()) {
                Toast.makeText(ResetActivity.this, "旧密码不正确", Toast.LENGTH_SHORT).show();
            } else {
                mContentResolver.update(Uri.parse(UserUtil.USERURI), values, "account = ?", new String[]{mLastAccount});
                Log.d(TAG, "onClick: account= " + mAccount + "  " + mOldPasword);
                Toast.makeText(ResetActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                intent.putExtra("accountFromReset", mLastAccount);
                setResult(2, intent);
                ResetActivity.this.finish();
            }

        }
    }

    /**
     * 判断该账号是否已存在
     *
     * @return 返回true，表示userInfo中已存在该账号，返回false，反之。
     */
    private boolean isExistAccount() {
        Cursor cursor = mContentResolver.query(Uri.parse(UserUtil.USERURI), null, "account = ? ", new String[]{mLastAccount}, null);
        return cursor.getCount() != 0;
    }

    /**
     * 判断旧密码是否正确
     * @return 是否正确
     */
    private boolean isCorrectPassword() {

        Cursor cursor = mContentResolver.query(Uri.parse(UserUtil.USERURI), null, "account = ? and password=? ", new String[]{mLastAccount, MD5.getMD5(mOldPasword) }, null);
        return cursor.getCount() != 0;
    }
}
