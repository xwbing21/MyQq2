package com.example.xue.myqq.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.xue.myqq.fragment.ContactsFragment;
import com.example.xue.myqq.fragment.MessageFragment;
import com.example.xue.myqq.fragment.TrendsFragment;
import com.example.xue.myqq.test.FirstActivity;

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

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
//        initEvent();
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

        //初始化过程直接用fragment替代activity
        FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
        resetAll();
        mSelectMessageButton.setSelected(true);
        mMessageFragment = new MessageFragment();
        transaction.replace(R.id.main_activity,mMessageFragment).commit();

    }

//    private void initEvent() {
//        //使用v4的包 所以一定要注意使用v4的Fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        mSelectMessageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "点击消息", Toast.LENGTH_SHORT).show();
//                if (mMessageFragment == null) {
//                    mMessageFragment = new MessageFragment();
//                    transaction.add(R.id.fragment_container_id, mMessageFragment);
//
//                } else {
//                    transaction.show(mMessageFragment);
//                }
//            }
//        });
//        mSelectContactsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mMessageFragment == null) {
//                    mContactsFragment = new ContactsFragment();
//                    fragmentTransaction.add(R.id.fragment_container_id, mContactsFragment);
//
//                } else {
//                    fragmentTransaction.show(mContactsFragment);
//                }
//
//            }
//        });
//        mSelectTrendsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mMessageFragment == null) {
//                    mTrendsFragment = new TrendsFragment();
//                    fragmentTransaction.add(R.id.fragment_container_id, mTrendsFragment);
//
//                } else {
//                    fragmentTransaction.show(mTrendsFragment);
//                }
//            }
//        });
//
//    }


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
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //使用v4的包 所以一定要注意使用v4的Fragment
        FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
//        hideAllFragment(transaction);
        switch (id) {
            case R.id.tv_select_message_id: {
                resetAll();
                mSelectMessageButton.setSelected(true);
                Toast.makeText(MainActivity.this, "点击消息", Toast.LENGTH_SHORT).show();
//                if (mMessageFragment == null) {
//                    mMessageFragment = new MessageFragment();
//                    transaction.add(R.id.fragment_container_id, mMessageFragment);
//
//                } else {
//                    transaction.show(mMessageFragment);
//                }
                mMessageFragment = new MessageFragment();
                transaction.replace(R.id.main_activity,mMessageFragment);
                break;
            }
            case R.id.tv_select_contacts_id: {
                resetAll();
                mSelectContactsButton.setSelected(true);
                Toast.makeText(MainActivity.this, "点击2", Toast.LENGTH_SHORT).show();
//                if (mContactsFragment == null) {
//                    mContactsFragment = new ContactsFragment();
//                    transaction.add(R.id.fragment_container_id, mContactsFragment);
//
//                } else {
//                    transaction.show(mContactsFragment);
//                }
            mContactsFragment = new ContactsFragment();
            transaction.replace(R.id.main_activity,mContactsFragment);
                break;

            }
            case R.id.tv_select_trends_id: {
                resetAll();
                mSelectTrendsButton.setSelected(true);
                Toast.makeText(MainActivity.this, "点击3", Toast.LENGTH_SHORT).show();
//                if (mContactsFragment == null) {
//                    mTrendsFragment = new TrendsFragment();
//                    transaction.add(R.id.fragment_container_id, mTrendsFragment);
//
//                } else {
//                    transaction.show(mTrendsFragment);
//                }
            mTrendsFragment = new TrendsFragment();
            transaction.replace(R.id.main_activity,mTrendsFragment);
                break;
            }

        }
        transaction.commit();

    }

    /**
     * 重置所有的选择为false
     */
    private void resetAll() {
        mSelectMessageButton.setSelected(false);
        mSelectContactsButton.setSelected(false);
        mSelectTrendsButton.setSelected(false);
    }
//    private void  hideAllFragment(FragmentTransaction transaction){
//        if (mMessageFragment!=null){
//            transaction.hide(mMessageFragment);
//        }else if (mContactsFragment!=null){
//            transaction.hide(mContactsFragment);
//        }else if (mTrendsFragment!=null){
//            transaction.hide(mTrendsFragment);
//
//        }
//    }

}

