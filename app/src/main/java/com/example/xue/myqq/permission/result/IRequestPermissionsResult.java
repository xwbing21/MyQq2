package com.example.xue.myqq.permission.result;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * 处理权限请求结果的接口
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/3/21 23:17
 **/
public interface IRequestPermissionsResult {
    /**
     * 权限请求处理结果
     * @author Sidney Ding
     * @date 2019/3/21 23:19
     * @param activity
     * @param permissions 请求权限的数组
     * @param grantResults 请求权限的结果数组
     * @return boolean
     **/
    boolean doRequestPermissionsResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults);
}
