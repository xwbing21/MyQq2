package com.example.xue.myqq.view.group;

import android.content.Context;

/**
 * TODO
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/24 11:42
 **/
public interface IGroupItemDecoration {
   Context getContext();
    /**
     * 判断当前点击位置是否处于GroupItem区域
     * @param x
     * @param y
     * @return
     */
    GroupItem findGroupItemUnder(int x, int y);
}
