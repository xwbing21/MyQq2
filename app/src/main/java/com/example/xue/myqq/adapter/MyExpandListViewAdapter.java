package com.example.xue.myqq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xue.myqq.R;
import com.example.xue.myqq.bean.FriendExpandInfo;
import com.example.xue.myqq.bean.FriendInfo;
import com.example.xue.myqq.view.CircleImageView;

import java.util.List;

/**
 * QQ分组的适配器
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/20 16:52
 **/
public class MyExpandListViewAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "ExpandListViewAdapter";

    private List<FriendExpandInfo> mExpandList;
    private Context mContex;

    public MyExpandListViewAdapter(Context context, List<FriendExpandInfo> expandList) {
        mContex = context;
        mExpandList = expandList;
    }

    @Override
    public int getGroupCount() {
        // 组数
        return mExpandList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // 子数
        return mExpandList.get(groupPosition).getFriendList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // 组对象
        return mExpandList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // 获得子对象
        return mExpandList.get(groupPosition).getFriendList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mExpandList.get(groupPosition).getFriendList().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        // 子条目ID相同时 是否复用
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // 分组Title的View设置
        if (convertView == null) {
            convertView = View.inflate(mContex, R.layout.friend_group_title_expand_list, null);
        }
        TextView tv_title = convertView.findViewById(R.id.tv_friend_group_title);
        tv_title.setText(mExpandList.get(groupPosition).getTitle());

        ImageView expand = convertView.findViewById(R.id.iv_expanded_friend_group);
        // 组是否展开
        if (isExpanded) {
            expand.setRotation(90);
            tv_title.setTextColor(Color.BLUE);
        } else {
            expand.setRotation(0);
            tv_title.setTextColor(Color.parseColor("#03081A"));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 子条目视图
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = View.inflate(mContex, R.layout.friend_group_child_expand_list, null);
            holder.portrait = convertView.findViewById(R.id.civ_portrait_friend);
            holder.nickName = convertView.findViewById(R.id.tv_nickname_friend);
            holder.sign = convertView.findViewById(R.id.tv_sign_friend);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        FriendInfo friendInfo = mExpandList.get(groupPosition).getFriendList().get(childPosition);
        holder.portrait.setImageBitmap(friendInfo.getPortrait());
        if (!TextUtils.isEmpty(friendInfo.getRemark())) {
            holder.nickName.setText(friendInfo.getRemark());
        } else {
            holder.nickName.setText(friendInfo.getNickname());
        }
        holder.sign.setText(friendInfo.getSign());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // 子条目是否可以被点击选中
        return true;
    }

    private static class ChildViewHolder {
        private CircleImageView portrait;
        private TextView nickName;
        private TextView sign;
    }
}
