package com.example.xue.myqq.permission.result;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

/**
 * 如果授权失败，不做任何处理
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/3/21 23:20
 **/
public class RequestPermissionsResult implements IRequestPermissionsResult {

    private RequestPermissionsResult() {
    }

    private static class SingleInstance {
        private static RequestPermissionsResult INSTACNE = new RequestPermissionsResult();
    }

    public static RequestPermissionsResult getInstance() {
        return SingleInstance.INSTACNE;
    }


    @Override
    public boolean doRequestPermissionsResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isAllGranted = true;
        // 判断是否所有的权限都已经授予了
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
                break;
            }
        }
        //已全部授权
        if (isAllGranted) {
            return true;
        }
        else {
            //什么也不做
        }
        return false;
    }
}
