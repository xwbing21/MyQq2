package com.example.xue.myqq.trends;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.xue.myqq.R;
import com.example.xue.myqq.bean.PictureViewInfo;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;


public class PreviewLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    public static final long ANIM_DURATION = 300;

    private View mBackgroundView;
    private GalleryViewPager mGalleryViewPager;
    private ImageView mScalableImageView;

    private List<PictureViewInfo> mPictureViewInfoList = new ArrayList<>();
    private int mIndex;
    private Rect mStartBounds = new Rect();
    private Rect mFinalBounds = new Rect();
    private boolean isAnimFinished = true;

    public PreviewLayout(Context context) {
        this(context, null);
    }

    public PreviewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.recyclerview_trends_gallery_previewlayout,
                this, true);
        mBackgroundView = findViewById(R.id.view_trends_gallery_preview_id);
        mGalleryViewPager = findViewById(R.id.viewpager_trends_gallery_preview_id);
        mScalableImageView = findViewById(R.id.image_trends_gallery_preview_id);

        mScalableImageView.setPivotX(0f);
        mScalableImageView.setPivotY(0f);
        mScalableImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    public void setData(List<PictureViewInfo> list, int index) {
        if (list == null || list.isEmpty() || index < 0) {
            return;
        }

        this.mPictureViewInfoList = list;
        this.mIndex = index;
        mStartBounds = mPictureViewInfoList.get(mIndex).getBounds();

        post(new Runnable() {
            @Override
            public void run() {
                mGalleryViewPager.setAdapter(new ImagePagerAdapter());
                mGalleryViewPager.setCurrentItem(mIndex);
                mGalleryViewPager.addOnPageChangeListener(PreviewLayout.this);

                mScalableImageView.setX(mStartBounds.left);
                mScalableImageView.setY(mStartBounds.top);
                Glide.with(getContext()).load(mPictureViewInfoList.get(mIndex).getUrl()).into(mScalableImageView);
            }
        });
    }

    public void startScaleUpAnimation() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Point globalOffset = new Point();
                getGlobalVisibleRect(mFinalBounds, globalOffset);
                mFinalBounds.offset(-globalOffset.x, -globalOffset.y);
                ViewGroup.LayoutParams lp = new LayoutParams(
                        mStartBounds.width(),
                        mStartBounds.width() * mFinalBounds.height() / mFinalBounds.width()
                );
                mScalableImageView.setLayoutParams(lp);
                startAnim();
                if (Build.VERSION.SDK_INT >= 16) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int position) {
        mIndex = position;
        mStartBounds = mPictureViewInfoList.get(mIndex).getBounds();
        if (mStartBounds == null) {
            return;
        }
        computeStartScale();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void startAnim() {
        if (!isAnimFinished) return;

        float startScale = computeStartScale();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mBackgroundView, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(mScalableImageView, View.X, mStartBounds.left, mFinalBounds.left),
                ObjectAnimator.ofFloat(mScalableImageView, View.Y, mStartBounds.top, mFinalBounds.top),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_X, 1f / startScale),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_Y, 1f / startScale));
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimFinished = false;
                mGalleryViewPager.setAlpha(0f);
                mScalableImageView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimFinished = true;
                mGalleryViewPager.setAlpha(1f);
                mScalableImageView.setVisibility(View.INVISIBLE);
            }
        });
        animatorSet.start();
    }

    private float computeStartScale() {
        float startScale;
        if ((float) mFinalBounds.width() / mFinalBounds.height()
                > (float) mStartBounds.width() / mStartBounds.height()) {
            // Extend start bounds horizontally （以竖直方向为参考缩放）
            startScale = (float) mStartBounds.height() / mFinalBounds.height();
            float startWidth = startScale * mFinalBounds.width();
            float deltaWidth = (startWidth - mStartBounds.width()) / 2;
            mStartBounds.left -= deltaWidth;
            mStartBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically （以水平方向为参考缩放）
            startScale = (float) mStartBounds.width() / mFinalBounds.width();
            float startHeight = startScale * mFinalBounds.height();
            float deltaHeight = (startHeight - mStartBounds.height()) / 2;
            mStartBounds.top -= deltaHeight;
            mStartBounds.bottom += deltaHeight;
        }
        return startScale;
    }

    public void startScaleDownAnimation() {
        if (!isAnimFinished) return;

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mScalableImageView, View.X, mStartBounds.left),
                ObjectAnimator.ofFloat(mScalableImageView, View.Y, mStartBounds.top),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_X, 1f),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_Y, 1f));
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimFinished = false;
                Glide.with(getContext()).load(mPictureViewInfoList.get(mIndex).getUrl()).into(mScalableImageView);
                mScalableImageView.setVisibility(View.VISIBLE);
                mGalleryViewPager.setAlpha(0f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimFinished = true;
                FrameLayout contentContainer = ((Activity) getContext())
                        .findViewById(android.R.id.content);//(FrameLayout)
                contentContainer.removeView(PreviewLayout.this);
            }
        });
        animatorSet.start();
    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPictureViewInfoList != null ? mPictureViewInfoList.size() : 0;
        }

        @Override
        public View instantiateItem(final ViewGroup container, final int position) {
            PhotoView photoView = new PhotoView(getContext());

            Glide.with(getContext()).load(mPictureViewInfoList.get(position).getUrl()).into(photoView);
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    startScaleDownAnimation();
                }
            });

            container.addView(photoView);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
