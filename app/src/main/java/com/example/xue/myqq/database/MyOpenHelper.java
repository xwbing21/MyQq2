package com.example.xue.myqq.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.xue.myqq.util.LogUtils;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "MyOpenHelper";

    private static final String DB_TABLE = "userInfo";
    private static final String DB_TABLE2 = "friendInfo";

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE friendInfo (Id Integer PRIMARY KEY AUTOINCREMENT, nickname varchar ( 255 )," +
                "remark varchar ( 255 ), account varchar ( 255 ), sex Integer ( 255 ), age varchar ( 255 )," +
                "sign varchar ( 255 ) );");
        db.execSQL("CREATE TABLE userInfo (id Integer PRIMARY KEY AUTOINCREMENT, account varchar ( 255 ),password varchar ( 255 ),sex Integer ( 255 ),phone varchar ( 255 ),address varchar ( 255 ) ,nikename varchar ( 255 ));");

        initFriendInfo(db);
    }

    /**
     * 伪造FriendInfo数据库
     *
     * @param db SQLiteDatabase
     */
    private void initFriendInfo(SQLiteDatabase db) {
        for (int i = 0; i < 100; i++) {
            int random = (int) (Math.random() * 10);
            if (random % 2 == 0) {
                db.execSQL("INSERT INTO friendInfo (nickname, remark, account, sex, age, sign) " +
                        "VALUES('Tom" + i + "', '小汤姆" + i + "', '" + (10001 + i) + "', '男', '16', '我是一条签名..')");
            } else {
                db.execSQL("INSERT INTO friendInfo (nickname, remark, account, sex, age, sign) " +
                        "VALUES('Tom" + i + "', '', '" + (10001 + i) + "', '男', '16', '我是一条签名..')");
            }
        }
        LogUtils.d(TAG, "initFriendInfo: FriendInfo表数据伪造完成..");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE2);
        onCreate(db);
    }
}
