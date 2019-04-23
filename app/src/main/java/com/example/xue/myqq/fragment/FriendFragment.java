package com.example.xue.myqq.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;
import butterknife.BindView;
import com.example.xue.myqq.R;
import com.example.xue.myqq.activity.ChattingActivity;
import com.example.xue.myqq.adapter.MyExpandListViewAdapter;
import com.example.xue.myqq.base.App;
import com.example.xue.myqq.base.BaseFragment;
import com.example.xue.myqq.bean.FriendExpandInfo;
import com.example.xue.myqq.bean.FriendInfo;
import com.example.xue.myqq.util.LogUtils;
import com.example.xue.myqq.view.MyExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示QQ好友列表
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/23 10:10
 **/
public class FriendFragment extends BaseFragment {
    private static final String TAG = "FriendFragment";

    @BindView(R.id.melv_list_friend)
    public MyExpandableListView mFriendListELV;
    // 好友数据
    private List<FriendInfo> mFriendInfoList;
    // 列表数据
    private List<FriendExpandInfo> mExpandList;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact_friend;
    }

    @Override
    protected void initData() {
        initFriendData();
        initExpandData();
        // 加适配器
        MyExpandListViewAdapter adapter = new MyExpandListViewAdapter(getActivity(), mExpandList);
        mFriendListELV.setAdapter(adapter);
        LogUtils.d(TAG, "initData: 已设置完适配器..");
        mFriendListELV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                FriendInfo friend = mExpandList.get(groupPosition).getFriendList().get(childPosition);
                // 跳到聊天界面
                Intent intent = new Intent();
                intent.setClass(App.getInstance(), ChattingActivity.class);
                intent.putExtra("friendInfo", friend);
                startActivity(intent);
                return true;
            }
        });
        mFriendListELV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "你想干啥..", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    /**
     * 初始化分组数据
     */
    private void initExpandData() {
        mExpandList = new ArrayList<>();

        List<FriendInfo> list1 = new ArrayList<>();
        FriendExpandInfo expandInfo1 = new FriendExpandInfo()
                .setTitle("第一组")
                .setFriendList(list1);
        List<FriendInfo> list2 = new ArrayList<>();
        FriendExpandInfo expandInfo2 = new FriendExpandInfo()
                .setTitle("第二组")
                .setFriendList(list2);
        List<FriendInfo> list3 = new ArrayList<>();
        FriendExpandInfo expandInfo3 = new FriendExpandInfo()
                .setTitle("第三组")
                .setFriendList(list3);
        List<FriendInfo> list4 = new ArrayList<>();
        FriendExpandInfo expandInfo4 = new FriendExpandInfo()
                .setTitle("第四组")
                .setFriendList(list4);
        List<FriendInfo> list5 = new ArrayList<>();
        FriendExpandInfo expandInfo5 = new FriendExpandInfo()
                .setTitle("第五组")
                .setFriendList(list5);

        int i = 0;
        for (FriendInfo friendInfo : mFriendInfoList) {
            if (i < 20) {
                list1.add(friendInfo);
            } else if (20 < i && i < 40) {
                list2.add(friendInfo);
            } else if (40 < i && i < 60) {
                list3.add(friendInfo);
            } else if (60 < i && i < 80) {
                list4.add(friendInfo);
            } else {
                list5.add(friendInfo);
            }
            i++;
        }

        mExpandList.add(expandInfo1);
        mExpandList.add(expandInfo2);
        mExpandList.add(expandInfo3);
        mExpandList.add(expandInfo4);
        mExpandList.add(expandInfo5);
        mExpandList.add(new FriendExpandInfo().setTitle("第六组").setFriendList(new ArrayList<FriendInfo>()));
        mExpandList.add(new FriendExpandInfo().setTitle("第七组").setFriendList(new ArrayList<FriendInfo>()));
        mExpandList.add(new FriendExpandInfo().setTitle("第八组").setFriendList(new ArrayList<FriendInfo>()));
        mExpandList.add(new FriendExpandInfo().setTitle("第九组").setFriendList(new ArrayList<FriendInfo>()));
        mExpandList.add(new FriendExpandInfo().setTitle("第十组").setFriendList(new ArrayList<FriendInfo>()));
        mExpandList.add(new FriendExpandInfo().setTitle("第十一组").setFriendList(new ArrayList<FriendInfo>()));
        mExpandList.add(new FriendExpandInfo().setTitle("第十二组").setFriendList(new ArrayList<FriendInfo>()));
        mExpandList.add(new FriendExpandInfo().setTitle("第13组").setFriendList(new ArrayList<FriendInfo>()));
        mExpandList.add(new FriendExpandInfo().setTitle("第14组").setFriendList(new ArrayList<FriendInfo>()));
    }

    /**
     * 初始化好友数据
     */
    private void initFriendData() {
        mFriendInfoList = new ArrayList<>();
        Uri uri = Uri.parse("content://com.example.xue.myqq.UserContentProviderprovider/friend");
        // 不是用 getActivity() 防止出现null
        ContentResolver resolver = App.getInstance().getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null);
        if (cursor != null) {
            FriendInfo friendInfo;
            Bitmap portrait = BitmapFactory.decodeResource(getResources(), R.mipmap.default_portrait);
            while (cursor.moveToNext()) {
                LogUtils.d(TAG, cursor.getString(1));
                friendInfo = new FriendInfo();
                int id = cursor.getInt(0);
                String nickname = cursor.getString(1);
                String remark = cursor.getString(2);
                String account = cursor.getString(3);
                String sex = cursor.getString(4);
                String age = cursor.getString(5);
                String sign = cursor.getString(6);
                LogUtils.d(TAG, "id" + id + " nickname" + nickname + " remark" + remark
                        + " account" + account + " sex" + sex + " age" + age + " sign" + sign);
                friendInfo.setId(id)
                        .setNickname(nickname)
                        .setRemark(remark)
                        .setAccount(account)
                        .setSex(sex)
                        .setAge(age)
                        .setSign(sign)
                        .setPortrait(portrait);
                mFriendInfoList.add(friendInfo);
            }
            cursor.close();
        }
    }
}
