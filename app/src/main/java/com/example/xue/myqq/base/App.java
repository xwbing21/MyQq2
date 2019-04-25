package com.example.xue.myqq.base;

import android.app.Application;
import com.example.xue.myqq.bean.ChatInfo;
import com.example.xue.myqq.bean.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Application
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/23 7:56
 **/
public class App extends Application {

    /**
     * 存放聊天信息的全局变量
     */
    public static List<List<ChatInfo>> msgList = new ArrayList<>();
    public static boolean isOpenContact = false;
    public static List<Contact> contactList = null;
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 外部获取单例
     *
     * @return Application
     */
    public static Application getInstance() {
        return instance;
    }
}
