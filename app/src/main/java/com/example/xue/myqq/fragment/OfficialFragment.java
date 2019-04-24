package com.example.xue.myqq.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.example.xue.myqq.R;
import com.example.xue.myqq.adapter.OfficialGroupAdapter;
import com.example.xue.myqq.base.App;
import com.example.xue.myqq.base.BaseFragment;
import com.example.xue.myqq.view.group.GroupItem;
import com.example.xue.myqq.view.group.GroupItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 公众号界面
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/24 11:15
 **/
public class OfficialFragment extends BaseFragment {

    @BindView(R.id.rv_official_contact)
    public RecyclerView mOfficialRV;

    private OfficialGroupAdapter adapter;
    private List<String> mOfficialNamelist = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact_official;
    }

    @Override
    protected void initData() {
        // 常规操作
        mOfficialRV.setLayoutManager(new LinearLayoutManager(getContext()));
        getOfficialData();
    }

    private void getOfficialData() {

        initOfficialNameList();

        adapter = new OfficialGroupAdapter(getContext(), mOfficialNamelist);
        mOfficialRV.setAdapter(adapter);

        // 开始使用GroupItemDecoration
        LayoutInflater layoutInflater = LayoutInflater.from(App.getInstance());
        View view = layoutInflater.inflate(R.layout.rv_item_group_official, null);
        GroupItemDecoration groupItemDecoration = new GroupItemDecoration(App.getInstance(), view, new GroupItemDecoration.DecorationCallback() {
            @Override
            public void setGroup(List<GroupItem> groupList) {
                initGroupList(groupList);
            }

            @Override
            public void buildGroupView(View groupView, GroupItem groupItem) {
                // 构建GroupView，通过view.findViewById找到内部控件，例如
                TextView textName = groupView.findViewById(R.id.tv_official_group_name);
                textName.setText(groupItem.getData("name").toString());
            }
        });

        mOfficialRV.addItemDecoration(groupItemDecoration);
    }

    /**
     * 设置公众号假数据
     */
    private void initOfficialNameList() {
        mOfficialNamelist.add("波动星球");  // B 0
        mOfficialNamelist.add("c语言·部落");    // C 1
        mOfficialNamelist.add("大学生英语·部落");  // D 2
        mOfficialNamelist.add("电脑技术分享·部落");
        mOfficialNamelist.add("高等数学·部落");   // G 4
        mOfficialNamelist.add("好友动态");      // H 5
        mOfficialNamelist.add("华夏黑客联盟·部落");
        mOfficialNamelist.add("几米漫画"); // J 7
        mOfficialNamelist.add("缴费充值");
        mOfficialNamelist.add("QQ安徽");  // Q 9
        mOfficialNamelist.add("QQ安全中心");
        mOfficialNamelist.add("QQ红包");
        mOfficialNamelist.add("QQ乐刮");
        mOfficialNamelist.add("QQ厘米秀");
        mOfficialNamelist.add("QQ钱包");
        mOfficialNamelist.add("QQ全城助力");
        mOfficialNamelist.add("QQ群主助手");
        mOfficialNamelist.add("QQ天气");
        mOfficialNamelist.add("QQ团队");
        mOfficialNamelist.add("QQ邮箱提醒");
        mOfficialNamelist.add("QQ运动");
        mOfficialNamelist.add("社工工程学·部落"); // S 21
        mOfficialNamelist.add("TIM团队"); // T 22
        mOfficialNamelist.add("腾讯课堂");
        mOfficialNamelist.add("腾讯新闻");
        mOfficialNamelist.add("中国红客联盟");  // Z 25
    }

    /**
     * 设置分组假数据
     *
     * @param groupList
     */
    private void initGroupList(List<GroupItem> groupList) {
        String[] groups = {"B", "C", "D", "G", "H", "J", "Q", "S", "T", "Z"};
        GroupItem tmp;
        for (int i = 0; i < mOfficialNamelist.size(); i++) {
            if (i < 1) {
                tmp = new GroupItem(0);
                tmp.setData("name", groups[0]);
                groupList.add(tmp);
            } else if (i < 2) {
                tmp = new GroupItem(1);
                tmp.setData("name", groups[1]);
                groupList.add(tmp);
            } else if (i < 4) {
                tmp = new GroupItem(2);
                tmp.setData("name", groups[2]);
                groupList.add(tmp);
            } else if (i < 5) {
                tmp = new GroupItem(4);
                tmp.setData("name", groups[3]);
                groupList.add(tmp);
            } else if (i < 7) {
                tmp = new GroupItem(5);
                tmp.setData("name", groups[4]);
                groupList.add(tmp);
            } else if (i < 9) {
                tmp = new GroupItem(7);
                tmp.setData("name", groups[5]);
                groupList.add(tmp);
            } else if (i < 21) {
                tmp = new GroupItem(9);
                tmp.setData("name", groups[6]);
                groupList.add(tmp);
            } else if (i < 22) {
                tmp = new GroupItem(21);
                tmp.setData("name", groups[7]);
                groupList.add(tmp);
            } else if (i < 25) {
                tmp = new GroupItem(22);
                tmp.setData("name", groups[8]);
                groupList.add(tmp);
            } else {
                tmp = new GroupItem(25);
                tmp.setData("name", groups[9]);
                groupList.add(tmp);
            }
        }
    }
}
