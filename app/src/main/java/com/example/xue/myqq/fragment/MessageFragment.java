package com.example.xue.myqq.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.xue.myqq.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageFragment extends Fragment {


    private ListView mMessageListView;
    private List<Map<String, String>> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null, false);
        mMessageListView = view.findViewById(R.id.lv_select_message_id);
        myAdapter myAdapter = new myAdapter(getActivity());
        mMessageListView.setAdapter(myAdapter);
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> map=new HashMap<String, String>();
            map.put("nikename", "nikename"+i);
            map.put("message", "message" + i);
            mList.add(map);
        }

        return view;
    }

    public class myAdapter extends BaseAdapter {
        Context context;
        public myAdapter(Context context) {
            this.context=context;
        }

        @Override
        public int getCount() {
            return 10;
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
            View view = null;
            //把我们的布局转换成一个view
            //我们可以通过打气筒把一个布局资源转换成一个view对象
            //resource 我们自己定义的布局
            if (convertView == null) {
                LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 view = inflater.inflate(R.layout.fragment_message_item, null);
            } else {
                view = convertView;
            }
            return view;
        }
    }

}
