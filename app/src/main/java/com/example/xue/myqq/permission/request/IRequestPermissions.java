package com.example.xue.myqq.permission.request;

import android.app.Activity;

/**
 * 权限请求接口
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/3/21 22:53
 **/
public interface IRequestPermissions {

    /**
     * 请求权限
     * @author Sidney Ding
     * @date 2019/3/21 22:56
     * @param activity 上下文
     * @param permissions 权限集合
     * @param resultCode 请求码
     * @return 如果权限请求全部允许，则返回true；反之，请求权限，引导用户开启权限
     **/
    boolean requestPermissions(Activity activity, String[] permissions, int resultCode);
}
