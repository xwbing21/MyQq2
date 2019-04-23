package com.example.xue.myqq.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import com.example.xue.myqq.R;
import com.example.xue.myqq.adapter.ContactAdapter;
import com.example.xue.myqq.base.App;
import com.example.xue.myqq.base.BaseFragment;
import com.example.xue.myqq.bean.Contact;
import com.example.xue.myqq.util.ContactUtils;
import com.example.xue.myqq.view.sidebar.HintIndexSidebar;
import com.example.xue.myqq.view.sidebar.IndexSidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 展示手机联系人
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/23 10:13
 **/
public class PhoneContactFragment extends BaseFragment implements IndexSidebar.OnChooseLetterChangedListener {
    private static final String TAG = "PhoneContactFragment";

    @BindView(R.id.rv_phone_contact)
    public RecyclerView mContactRV;
    @BindView(R.id.his_contact_search)
    public HintIndexSidebar indexSidebar;

    private List<Contact> mContactList;
    private ContactAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ContactUtils mContactUtils;

    private String phoneNum;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact_phone_contact;
    }

    @Override
    protected void initData() {
        mContactUtils = new ContactUtils(App.getInstance());
        mContactList = new ArrayList<>();
        indexSidebar.setOnChooseLetterChangedListener(this);
        // RecyclerView常规操作
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mContactRV.setLayoutManager(mLayoutManager);
        // 设置适配器
        mAdapter = new ContactAdapter(App.getInstance(), mContactList);

        mAdapter.setOnItemClickListener(new ContactAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                phoneNum = mContactList.get(postion).getPhoneNum();
                showDialog();
                Log.d(TAG, "onItemClick: 点击监听" + phoneNum);
            }
        });
        getContactData();
        mContactRV.setAdapter(mAdapter);
    }

    private static String[] items = {"打电话", "发短信"};

    private void showDialog() {
        new AlertDialog.Builder(getContext())
                .setItems(items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        dialing(phoneNum);
                                        break;
                                    case 1:
                                        enterSmsUI(phoneNum);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();
    }

    private void getContactData() {
        // 获取所有联系人
        mContactList = mContactUtils.readAll();
        Collections.sort(mContactList);
        mAdapter.setData(mContactList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChooseLetter(String s) {
        int i = mAdapter.getFirstPositionByChar(s);
        Log.d(TAG, "onChooseLetter: " + i);
        if (i == -1) {
            return;
        }
        mLayoutManager.scrollToPositionWithOffset(i, 0);
    }

    @Override
    public void onNoChooseLetter() {
        // 什么也不做
    }

    /**
     * 拨打电话
     *
     * @param phoneNum 电话号码
     * @return void
     **/
    private void dialing(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
        if (ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            Toast.makeText(App.getInstance(), "请给APP授权，否则功能无法正常使用！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 进入发送短信界面
     *
     * @param phoneNum 电话号码
     * @return void
     **/
    public void enterSmsUI(String phoneNum) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNum));
        startActivity(intent);
    }
}
