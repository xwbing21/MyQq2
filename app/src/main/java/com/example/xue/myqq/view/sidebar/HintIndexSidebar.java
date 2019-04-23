package com.example.xue.myqq.view.sidebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.xue.myqq.R;

/**
 * 选中侧边索引栏的文字 在屏幕中间提示选中文本
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/21 22:41
 **/
public class HintIndexSidebar extends RelativeLayout implements IndexSidebar.OnChooseLetterChangedListener {
    private TextView hintText;
    private IndexSidebar.OnChooseLetterChangedListener listener;

    public HintIndexSidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 布局文件不能少
        LayoutInflater.from(context).inflate(R.layout.view_hint_index_sidebar, this);
        initView();
    }

    private void initView() {
        IndexSidebar indexSidebar = findViewById(R.id.index_sidebar);
        hintText = findViewById(R.id.hint_index_sidebar_tv);
        indexSidebar.setOnTouchingLetterChangedListener(this);
    }

    @Override
    public void onChooseLetter(String s) {
        hintText.setText(s);
        hintText.setVisibility(VISIBLE);
        if (listener != null) {
            listener.onChooseLetter(s);
        }
    }

    @Override
    public void onNoChooseLetter() {
        hintText.setVisibility(INVISIBLE);
        if (listener != null) {
            listener.onNoChooseLetter();
        }
    }

    public void setOnChooseLetterChangedListener(IndexSidebar.OnChooseLetterChangedListener listener) {
        this.listener = listener;
    }
}
