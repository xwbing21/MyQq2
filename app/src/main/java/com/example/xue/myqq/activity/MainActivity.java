package com.example.xue.myqq.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xue.myqq.R;
import com.example.xue.myqq.base.App;
import com.example.xue.myqq.fragment.ContactsFragment;
import com.example.xue.myqq.fragment.MessageFragment;
import com.example.xue.myqq.fragment.TrendsFragment;
import com.example.xue.myqq.permission.PermissionUtils;
import com.example.xue.myqq.permission.request.IRequestPermissions;
import com.example.xue.myqq.permission.request.RequestPermissions;
import com.example.xue.myqq.permission.result.IRequestPermissionsResult;
import com.example.xue.myqq.permission.result.RequestPermissionsResultSetApp;
import com.example.xue.myqq.test.FirstActivity;
import com.example.xue.myqq.util.UserUtil;
import com.example.xue.myqq.view.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private TextView mSelectMessageButton;
    private TextView mSelectContactsButton;
    private TextView mSelectTrendsButton;
    private MessageFragment mMessageFragment;
    private ContactsFragment mContactsFragment;
    private TrendsFragment mTrendsFragment;
    private FragmentManager mSupportFragmentManager;
    private CircleImageView mUserIconImageView;
    private NavigationView mNavigationView;
    private TextView mNavagationNikenameTextView;
    // 权限相关
    private String[] permissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS};
    private IRequestPermissions requestPermissions = RequestPermissions.getInstance();
    private IRequestPermissionsResult requestPermissionsResult = RequestPermissionsResultSetApp.getInstance();

    private void initBefore() {
        boolean flag = requestPermissions.requestPermissions(this, permissions, PermissionUtils.ResultCode1);
        // 不给权限直接挂
        // if (!flag) {
        //     finish();
        // }
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 给我权限 谢谢
        initBefore();
        setContentView(R.layout.activity_main);
//        CircleImageView qq_usr_icon = findViewById(R.id.qq_usr_icon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //替换掉原有的actionbar
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_icon);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_id);
        navigationView.setNavigationItemSelectedListener(this);

        init();
//        initEvent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 用户给APP授权的结果
        // 判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if (requestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults)) {
            // 请求的权限全部授权成功，此处可以做自己想做的事了
            // 输出授权结果
            // Toast.makeText(App.getInstance(), "授权成功，请重新点击刚才的操作！", Toast.LENGTH_LONG).show();
        } else {
            // 输出授权结果
            Toast.makeText(this, "请给APP授权，否则功能无法正常使用！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 初始化view及界面
     */
    private void init() {
        mSelectMessageButton = findViewById(R.id.tv_select_message_id);
        mSelectContactsButton = findViewById(R.id.tv_select_contacts_id);
        mSelectTrendsButton = findViewById(R.id.tv_select_trends_id);
        mSelectMessageButton.setOnClickListener(this);
        mSelectContactsButton.setOnClickListener(this);
        mSelectTrendsButton.setOnClickListener(this);
        mSupportFragmentManager = getSupportFragmentManager();

        //调用mNavigationView中头部的控件，一定要精确
        mNavigationView = findViewById(R.id.nav_view_id);
        View headerView = mNavigationView.getHeaderView(0);
        mUserIconImageView = headerView.findViewById(R.id.qq_usr_icon_id);
        mNavagationNikenameTextView = headerView.findViewById(R.id.tv_navagation_nikename_id);
        mNavagationNikenameTextView.setText(getNikeFromUserInfo());
        mUserIconImageView.setOnClickListener(this);

        //初始化过程直接用fragment替代activity
        FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
        resetAll();
        mSelectMessageButton.setSelected(true);
        mMessageFragment = new MessageFragment();
        transaction.replace(R.id.main_activity, mMessageFragment).commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.toolbar_addfriend: {
//                跳轉到添加好友的頁面
//                 Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.toolbar_addgroup: {
                //跳轉到添加好友的頁面
//                new Intent(MainActivity.this);

            }

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_exit_login_id) {
            SharedPreferences loginState = getSharedPreferences("loginState", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = loginState.edit();
            edit.putBoolean("loginExist", false);
            edit.commit();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
            Log.d(TAG, "onNavigationItemSelected:  nav_exit_login_id");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * MainActivity点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        //使用v4的包 所以一定要注意使用v4的Fragment
        FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
        switch (id) {
            case R.id.tv_select_message_id: {
                resetAll();
                mSelectMessageButton.setSelected(true);
                Toast.makeText(MainActivity.this, "点击消息", Toast.LENGTH_SHORT).show();
                mMessageFragment = new MessageFragment();
                transaction.replace(R.id.main_activity, mMessageFragment);
                break;
            }
            case R.id.tv_select_contacts_id: {
                resetAll();
                mSelectContactsButton.setSelected(true);
                Toast.makeText(MainActivity.this, "点击2", Toast.LENGTH_SHORT).show();
                mContactsFragment = new ContactsFragment();
                transaction.replace(R.id.main_activity, mContactsFragment);
                break;

            }
            case R.id.tv_select_trends_id: {
                resetAll();
                mSelectTrendsButton.setSelected(true);
                Toast.makeText(MainActivity.this, "点击3", Toast.LENGTH_SHORT).show();
                mTrendsFragment = new TrendsFragment();
                transaction.replace(R.id.main_activity, mTrendsFragment);
                break;
            }
            case R.id.qq_usr_icon_id: {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
                break;
            }

        }
        transaction.commit();

    }

    /**
     * 从userInfo中获取nikename
     * @return nikename（String）
     */
    private String getNikeFromUserInfo() {
        ContentResolver contentResolver = getContentResolver();
        String acountFromSP = getAcountFromSP();
        String nikename = null;
        Cursor cursor = contentResolver.query(Uri.parse(UserUtil.USERURI), null, "account = ?", new String[]{acountFromSP}, null);
        if (cursor.moveToFirst()) {
            do {
                nikename = cursor.getString(cursor.getColumnIndex("nikename"));
            } while (cursor.moveToNext());

        }
        return nikename;
    }

    /**
     * 从sp中获取当前account
     * @return 当前account（String）
     */
    private String getAcountFromSP() {
        SharedPreferences sp = getSharedPreferences("account", Context.MODE_PRIVATE);
        String account = sp.getString("accountId", "wrong");
        return account;
    }


    /**
     * 重置所有的选择为false（底部菜单栏）
     */
    private void resetAll() {
        mSelectMessageButton.setSelected(false);
        mSelectContactsButton.setSelected(false);
        mSelectTrendsButton.setSelected(false);
    }


}

