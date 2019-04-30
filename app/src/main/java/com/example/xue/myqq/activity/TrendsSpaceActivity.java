package com.example.xue.myqq.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.xue.myqq.R;
import com.example.xue.myqq.adapter.TrendsSpaceAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrendsSpaceActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TrendsSpaceAdapter mTrendsSpaceAdapter;
    private List<String> Urls = new ArrayList<>();//存储图片路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends_spacectivity);

        initUrls();//初始化图片数据
        mRecyclerView = findViewById(R.id.recyclerview_trends_space_id);//获取RecyclerView布局
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);//设置RecyclerView的布局管理器
        mTrendsSpaceAdapter = new TrendsSpaceAdapter(Urls);
        mRecyclerView.setAdapter(mTrendsSpaceAdapter);//设置RecyclerView的适配器
    }

    public void initUrls() {
        Urls.add("/storage/emulated/0/0001/21.jpg");
        Urls.add("/storage/emulated/0/0001/24.jpg");
        Urls.add("/storage/emulated/0/0001/23.jpg");
        Urls.add("/storage/emulated/0/0001/21.jpg");
        Urls.add("/storage/emulated/0/0001/24.jpg");
        Urls.add("/storage/emulated/0/0001/23.jpg");
    }
}
