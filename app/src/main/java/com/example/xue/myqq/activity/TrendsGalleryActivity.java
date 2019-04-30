package com.example.xue.myqq.activity;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.xue.myqq.R;
import com.example.xue.myqq.bean.ImageUrlConfig;
import com.example.xue.myqq.bean.PictureViewInfo;
import com.example.xue.myqq.trends.PreviewLayout;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class TrendsGalleryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayout.LayoutParams mLayoutParams;
    private PreviewLayout mPreviewLayout;
    private FrameLayout mFrameLayout;
    private List<PictureViewInfo> mPictureViewInfoList = new ArrayList<>();

    private int mStatusBarHeight;
    private int[] mPadding = new int[4];
    private int mSolidWidth = 0;
    private int mSolidHeight = 0;
    private Rect mRVBounds = new Rect();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends_gallery);

        mRecyclerView = findViewById(R.id.recyclerview_trends_gallery_id);
        mFrameLayout = findViewById(android.R.id.content);

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        mLayoutParams = new LinearLayout.LayoutParams(metrics.widthPixels / 3,
                metrics.widthPixels / 3);

        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mStatusBarHeight = getResources().getDimensionPixelSize(resId);

        List<String> urls = ImageUrlConfig.getUrls();
        for (int i = 0; i < urls.size(); i++) {
            mPictureViewInfoList.add(new PictureViewInfo(urls.get(i), i));
        }
        mGridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new MyAdapter());

        mRecyclerView.addOnScrollListener(new MyRecyclerOnScrollListener());
    }

    public void myItemClick(View view) {
        int position = mRecyclerView.getChildAdapterPosition(view);
        mPreviewLayout = new PreviewLayout(TrendsGalleryActivity.this);
        mPreviewLayout.setData(mPictureViewInfoList, position);
        mPreviewLayout.startScaleUpAnimation();
        mFrameLayout.addView(mPreviewLayout);
    }

    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.recyclerview_trends_gallery, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ImageView imageView = holder.imageView;
            holder.itemView.setLayoutParams(mLayoutParams);
            Glide.with(TrendsGalleryActivity.this)
                    .load(mPictureViewInfoList.get(position).getUrl())
                    .into(imageView);
//            mExecutorService.execute(new LoadImage(imageView,
//                    mPictureViewInfoList.get(position).getUrl(),new Handler()));
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mPictureViewInfoList.size();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_trends_gallery_thumb_id);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mRecyclerView.getGlobalVisibleRect(mRVBounds);

            assembleDataList();
        }
    }

    private class MyRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                assembleDataList();
            }
        }
    }

    private void assembleDataList() {
        computeBoundsForward(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
        computeBoundsBackward(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
    }

    /**
     * 从第一个完整可见item顺序遍历
     */
    private void computeBoundsForward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < mPictureViewInfoList.size(); i++) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = itemView.findViewById(R.id.image_trends_gallery_thumb_id);
                thumbView.getGlobalVisibleRect(bounds);

                if (mSolidWidth * mSolidHeight == 0) {
                    mPadding[0] = thumbView.getPaddingLeft();
                    mPadding[1] = thumbView.getPaddingTop();
                    mPadding[2] = thumbView.getPaddingRight();
                    mPadding[3] = thumbView.getPaddingBottom();
                    mSolidWidth = bounds.width();
                    mSolidHeight = bounds.height();
                }

                bounds.left = bounds.left + mPadding[0];
                bounds.top = bounds.top + mPadding[1];
                bounds.right = bounds.left + mSolidWidth - mPadding[2];
                bounds.bottom = bounds.top + mSolidHeight - mPadding[3];
            } else {
                bounds.left = i % 3 * mSolidWidth + mPadding[0];
                bounds.top = mRVBounds.bottom + mPadding[1];
                bounds.right = bounds.left + mSolidWidth - mPadding[2];
                bounds.bottom = bounds.top + mSolidHeight - mPadding[3];
            }
            bounds.offset(0, -mStatusBarHeight);

            mPictureViewInfoList.get(i).setBounds(bounds);
        }
    }

    /**
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos - 1; i >= 0; i--) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();

            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.image_trends_gallery_thumb_id);

                thumbView.getGlobalVisibleRect(bounds);

                bounds.left = bounds.left + mPadding[0];
                bounds.bottom = bounds.bottom - mPadding[3];
                bounds.right = bounds.left + mSolidWidth - mPadding[2];
                bounds.top = bounds.bottom - mSolidHeight + mPadding[1];
            } else {
                bounds.left = i % 3 * mSolidWidth + mPadding[0];
                bounds.bottom = mRVBounds.top - mPadding[3];
                bounds.right = bounds.left + mSolidWidth - mPadding[2];
                bounds.top = bounds.bottom - mSolidHeight + mPadding[1];
            }
            bounds.offset(0, -mStatusBarHeight);
            mPictureViewInfoList.get(i).setBounds(bounds);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFrameLayout.getChildAt(mFrameLayout.getChildCount() - 1) instanceof PreviewLayout) {
            mPreviewLayout.startScaleDownAnimation();
            return;
        }
        super.onBackPressed();
    }
}
