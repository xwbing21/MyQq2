package com.example.xue.myqq.permission.request;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * TODO
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/3/21 22:59
 **/
public class RequestPermissions implements IRequestPermissions {
    private RequestPermissions() {
    }

    private static class SingleInstance {
        private static RequestPermissions INSTANCE = new RequestPermissions();
    }

    public static RequestPermissions getInstance() {
        return SingleInstance.INSTANCE;
    }


    @Override
    public boolean requestPermissions(Activity activity, String[] permissions, int resultCode) {
        // 判断手机版本是否23以下，如果是，不需要使用动态权限
        if(Build.VERSION.SDK_INT < 23){
            return true;
        }
        //判断并请求权限
        return requestNeedPermission(activity, permissions,resultCode);
    }

    /**
     * 请求所有指定的权限
     * @author Sidney Ding
     * @date 2019/3/21 23:12
     * @param activity
     * @param permissions
     * @param resultCode
     * @return boolean
     **/
    private boolean requestAllPermission(Activity activity, String[] permissions, int resultCode){
        //判断是否已赋予了全部权限
        boolean isAllGranted = CheckPermission.checkPermissionAllGranted(activity, permissions);
        if(isAllGranted){
            return true;
        }
        ActivityCompat.requestPermissions(activity, permissions, resultCode);
        return false;
    }

    private boolean requestNeedPermission(Activity activity, String[] permissions, int resultCode){
        List<String> list = CheckPermission.checkPermissionDenied(activity, permissions);
        if(list.size() == 0){
            return true;
        }
        //请求权限
        String[] deniedPermissions = list.toArray(new String[list.size()]);
        ActivityCompat.requestPermissions(activity, deniedPermissions, resultCode);
        return false;
    }
}
