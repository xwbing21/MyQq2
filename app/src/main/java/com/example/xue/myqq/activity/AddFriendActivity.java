package com.example.xue.myqq.activity;

import android.content.ContentResolver;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import com.example.xue.myqq.R;
import com.example.xue.myqq.base.BaseActivity;
import com.example.xue.myqq.database.MyOpenHelper;
import org.w3c.dom.Text;

public class AddFriendActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_account_add_friend)
    public EditText mAccountET;
    @BindView(R.id.iv_clear_account_add_friend)
    public ImageView mClearAccountIV;
    @BindView(R.id.et_nickname_add_friend)
    public EditText mNicknameET;
    @BindView(R.id.iv_clear_nickname_add_friend)
    public ImageView mClearNicknameIV;
    @BindView(R.id.btn_add_friend)
    public Button mAddFriendBTN;

    private ContentResolver resolver;
    public static Uri uri = Uri.parse("content://com.example.xue.myqq.UserContentProviderprovider/friend");

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void initData() {
        resolver = getContentResolver();
        initEvent();
    }

    private void initEvent() {
        // 监听有没有输入文字
        mAccountET.addTextChangedListener(new MyTextWatcher(mAccountET));
        mNicknameET.addTextChangedListener(new MyTextWatcher(mNicknameET));
        // 监听点击
        mClearAccountIV.setOnClickListener(this);
        mClearNicknameIV.setOnClickListener(this);
        mAddFriendBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_account_add_friend:
                mAccountET.setText("");
                break;
            case R.id.iv_clear_nickname_add_friend:
                mNicknameET.setText("");
                break;
            case R.id.btn_add_friend:
                addFriend();
                break;
            default:
                break;
        }
    }

    /**
     * 添加好友
     */
    private void addFriend() {
        String account = mAccountET.getText().toString();
        String nickname = mNicknameET.getText().toString();
        if (notNull(account, nickname)) {
            return;
        }
        Toast.makeText(this, "添加成功!", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * 判断是否有输入
     * @param account
     * @param nickname
     * @return
     */
    private boolean notNull(String account, String nickname) {
        if (TextUtils.isEmpty(account)) {
            shake(mAccountET);
            mAccountET.setHintTextColor(Color.RED);
            return true;
        }
        if (TextUtils.isEmpty(nickname)) {
            shake(mNicknameET);
            mNicknameET.setHintTextColor(Color.RED);
            return true;
        }
        return false;
    }

    private void shake(@NonNull View v) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        v.startAnimation(animation);
    }

    /**
     * 监听文本变化
     */
    class MyTextWatcher implements TextWatcher {

        EditText mView;
        MyTextWatcher(EditText view) {
            mView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (mView.getId()) {
                case R.id.et_account_add_friend:
                    visibility(mClearAccountIV, mView);
                    break;
                case R.id.et_nickname_add_friend:
                    visibility(mClearNicknameIV, mView);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        /**
         * 设置可见
         * @param iv
         * @param et
         */
        void visibility(ImageView iv, EditText et) {
            if (TextUtils.isEmpty(et.getText().toString())) {
                iv.setVisibility(View.GONE);
            } else {
                iv.setVisibility(View.VISIBLE);
            }
        }
    }
}
