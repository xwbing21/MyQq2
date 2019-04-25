package com.example.xue.myqq.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xue.myqq.R;
import com.example.xue.myqq.adapter.ChatListViewAdapter;
import com.example.xue.myqq.base.App;
import com.example.xue.myqq.base.BaseActivity;
import com.example.xue.myqq.bean.ChatInfo;
import com.example.xue.myqq.bean.FriendInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChattingActivity extends BaseActivity {

    @BindView(R.id.lv_chat_dialog)
    public ListView mChatDialogListView;
    @BindView(R.id.et_chat_message)
    public EditText mChatMessageEditText;
    @BindView(R.id.btn_chat_message_send)
    public Button mSendMessageButton;
    // 假设这是自己的头像
    private Bitmap mToPortrait;
    // 聊天信息集合
    private List<ChatInfo> mChatList;

    private Handler handler;
    private ChatListViewAdapter adapter;
    private FriendInfo mFriendInfo;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_chatting;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        // 聊天对象消息
        mFriendInfo = intent.getParcelableExtra("friendInfo");
        mToPortrait = BitmapFactory.decodeResource(getResources(), R.mipmap.header_q);
        mChatList = new ArrayList<>();
        handler = new MyHandler(this);

        if (mFriendInfo != null){
            Bitmap friendPortrait = mFriendInfo.getPortrait();
            for (int i = 0; i < 3; i++) {
                ChatInfo chat = new ChatInfo()
                        .setNickname(mFriendInfo.getNickname())
                        .setMeSend(false)
                        .setChatMessage("Hello World!")
                        .setPortrait(friendPortrait);
                mChatList.add(chat);
            }
        }else {
            int position = intent.getIntExtra("position", 0);
            List<ChatInfo> chatInfos = App.msgList.get(position);
            mChatList=chatInfos;
        }

        adapter = new ChatListViewAdapter(this, mChatList);
        mChatDialogListView.setAdapter(adapter);
        initEvent();
    }

    /**
     * 初始化事件 发送按钮事件
     */
    private void initEvent() {
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mChatMessageEditText.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(ChattingActivity.this, "发送消息不能为空!", Toast.LENGTH_LONG).show();
                } else {
                    ChatInfo chat = new ChatInfo()
                            .setPortrait(mToPortrait)
                            .setChatMessage(message)
                            .setMeSend(true);
                    mChatList.add(chat);
                    // 清空输入框
                    mChatMessageEditText.setText("");
                    adapter.notifyDataSetChanged();
                    handler.sendEmptyMessage(1);
                    if (mFriendInfo!=null){
                        // 把消息加进App全局变量
                        App.msgList.add(mChatList);
                    }

                }
            }
        });
    }

    static class MyHandler extends Handler {

        WeakReference<ChattingActivity> reference;

        private MyHandler(ChattingActivity chattingActivity) {
            reference = new WeakReference<>(chattingActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChattingActivity chattingActivity = reference.get();
            if (msg.what == 1) {
                // 控制条目在最后一行
                chattingActivity.mChatDialogListView.setSelection(chattingActivity.mChatList.size());
            }
        }
    }
}
