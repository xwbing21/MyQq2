package com.example.xue.myqq.util;

import android.util.Log;

public class LogUtils {
    private static boolean flag = true;
    
    public static void i(String tag, String message) {
        if (flag) {
            Log.i(tag, message);
        }
    }
    
    public static void d(String tag, String message) {
        if (flag) {
            Log.d(tag, message);
        }
    }
    
    public static void w(String tag, String message) {
        if (flag) {
            Log.w(tag, message);
        }
    }
    
    public static void e(String tag, String message) {
        if (flag) {
            Log.e(tag, message);
        }
    }
}