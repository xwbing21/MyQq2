package com.example.xue.myqq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.xue.myqq.R;
import com.example.xue.myqq.bean.ChatInfo;
import com.example.xue.myqq.view.CircleImageView;

import java.util.List;

/**
 * 聊天的ListView的Adapter
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/21 11:08
 **/
public class ChatListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChatInfo> mChatList;

    public ChatListViewAdapter(Context context, List<ChatInfo> chatList) {
        super();
        mContext = context;
        mChatList = chatList;
    }

    /**
     * 是否为自己发送的消息
     */
    public static interface IMsgViewType {
        int IMVT_FROM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }

    @Override
    public int getCount() {
        return mChatList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
     */
    public int getItemViewType(int position) {
        ChatInfo entity = mChatList.get(position);
        // 收到的消息
        if (entity.isMeSend()) {
            return IMsgViewType.IMVT_FROM_MSG;
        } else {
            // 自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView holder;
        ChatInfo entity = mChatList.get(position);
        boolean meSend = entity.isMeSend();
        // 当前视图 为空 或者 当前视图不符合
        if (convertView == null || (holder = (HolderView) convertView.getTag()).isMeSend != meSend) {
            holder = new HolderView();
            if (meSend) {
                convertView = View.inflate(mContext, R.layout.chat_to_item, null);
                holder.civ_chat_icon = convertView.findViewById(R.id.civ_chat_to_portrait);
                holder.tv_chat_msg = convertView.findViewById(R.id.tv_chat_to_msg);
                holder.isMeSend = true;
            } else {
                convertView = View.inflate(mContext, R.layout.chat_from_item, null);
                holder.civ_chat_icon = convertView.findViewById(R.id.civ_chat_from_portrait);
                holder.tv_chat_msg = convertView.findViewById(R.id.tv_chat_from_msg);
                holder.isMeSend = false;
            }
            convertView.setTag(holder);
        }

        holder.civ_chat_icon.setImageBitmap(mChatList.get(position).getPortrait());
        holder.tv_chat_msg.setText(mChatList.get(position).getChatMessage());

        return convertView;
    }

    class HolderView {
        CircleImageView civ_chat_icon;
        TextView tv_chat_msg;
        boolean isMeSend;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
