package com.example.xue.myqq.permission.result;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import com.example.xue.myqq.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 如果授权失败，引导用户进行应用授权
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/3/21 23:25
 **/
public class RequestPermissionsResultSetApp implements IRequestPermissionsResult {
    private RequestPermissionsResultSetApp() {
    }

    private static class SingleInstance {
        private static RequestPermissionsResultSetApp INSTANCE = new RequestPermissionsResultSetApp();
    }

    public static RequestPermissionsResultSetApp getInstance() {
        return SingleInstance.INSTANCE;
    }


    @Override
    public boolean doRequestPermissionsResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> deniedPermission = new ArrayList<>();
        for (int i=0; i<grantResults.length;i++){
            if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                deniedPermission.add(permissions[i]);
            }
        }
        //已全部授权
        if (deniedPermission.size() == 0) {
            return true;
        }
        //引导用户去授权
        else {
            String name = PermissionUtils.getInstance().getPermissionNames(deniedPermission);
            SetPermissions.openAppDetails(activity,name);
        }
        return false;
    }
}
