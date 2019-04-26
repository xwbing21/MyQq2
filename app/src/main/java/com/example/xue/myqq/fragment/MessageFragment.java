package com.example.xue.myqq.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xue.myqq.R;
import com.example.xue.myqq.activity.ChattingActivity;
import com.example.xue.myqq.base.App;
import com.example.xue.myqq.bean.ChatInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";
    private ListView mMessageListView;
    private List<Map<String, String>> mList;
    private List<ChatInfo> mChatInfoList;
    private int mListViewCount;
    public MessageFragment.myAdapter myAdapter;
    private List<List<ChatInfo>> mMessageList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null, false);
        mMessageListView = view.findViewById(R.id.lv_select_message_id);
        mListViewCount = -1;
        mMessageList = new ArrayList<>();
        mMessageList =App.msgList;
        Log.d(TAG, "onCreateView: mMessageList start"+mMessageList.size());

        myAdapter = new myAdapter(getActivity(), mChatInfoList);
        mMessageListView.setAdapter(myAdapter);

        //点击事件
        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),ChattingActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

        //长点击事件
        mMessageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(getActivity(),"长按点击1",Toast.LENGTH_SHORT).show();
                final int removePosition=position;
                if (App.msgList.size()<=0){
                    Log.d(TAG, "onItemLongClick:size为空 ");
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("小提示");
                    builder.setMessage("是否移除该条消息");
                    builder.setIcon(R.drawable.ic_qq_icon);
                    builder.setCancelable(true);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.msgList.remove(removePosition);
                            mMessageList=App.msgList;
                            Log.d(TAG, "onItemLongClick: list 2"+mMessageList.size());
                            myAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onItemLongClick: position"+position);
                        }
                    });
                    builder.show();
                }

                return true;
            }
        });


        return view;
    }

    public class myAdapter extends BaseAdapter {
        Context context;
        private ViewHolder myHolder;

        public myAdapter(Context context, List<ChatInfo> list) {
            this.context=context;
        }

        @Override
        public int getCount() {
            return mMessageList.size()>0 ?mMessageList.size():0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.fragment_message_item, null);

                myHolder = new ViewHolder();
                myHolder.nicknameTextView = convertView.findViewById(R.id.iv_fragment_item_nikename_id);
                myHolder.messageTextView = convertView.findViewById(R.id.iv_fragment_item_message_id);
                convertView.setTag(myHolder);
            } else {
                myHolder = (ViewHolder) convertView.getTag();
            }
            if (getCount()<=0){
                Toast.makeText(context.getApplicationContext(),"没有数据",Toast.LENGTH_SHORT);
            }else {
                Log.d(TAG, "getView: getcount"+getCount());
                List<ChatInfo> chatInfos = mMessageList.get(position);
                myHolder.nicknameTextView.setText(chatInfos.get(2).getNickname());
                myHolder.messageTextView.setText(chatInfos.get(chatInfos.size()-1).getChatMessage());
                Log.d(TAG, "getView: nicknameTextView messageTextView"+chatInfos.get(3).getNickname()+","+chatInfos.get(3).getChatMessage());
            }

            return convertView;
        }

    }
     class ViewHolder{
        TextView nicknameTextView;
        TextView messageTextView;
    }

}
