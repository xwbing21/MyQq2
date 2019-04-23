package com.example.xue.myqq.anim;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * PapgerView滑动时的动画
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/19 17:55
 **/
public class MyPagerTransition implements ViewPager.PageTransformer {

    /**
     * 页面切换动画
     *
     * @param page     页面
     * @param position -1左页 0当前页 1右页
     */
    @Override
    public void transformPage(@NonNull View page, float position) {
        float MIN_SCALE = 0.75f;
        float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
        if (position > 1 || position < -1) {
            page.setAlpha(0f);
        } else if (position <= 0) {
            page.setAlpha(1 + position);
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else if (position <= 1) {
            page.setAlpha(1 - position);
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        }
    }
}
