package com.example.xue.myqq.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String DB_TABLE = "userInfo";

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE friendInfo (Id Integer PRIMARY KEY AUTOINCREMENT,nickname varchar ( 255 )," +
                "remark varchar ( 255 ),account varchar ( 255 ),sex Integer ( 255 ),age varchar ( 255 )," +
                "sign varchar ( 255 ) );");
        db.execSQL("CREATE TABLE userInfo (id Integer PRIMARY KEY AUTOINCREMENT,account varchar ( 255 )," +
                "password varchar ( 255 ),sex varchar ( 255 ),phone varchar ( 255 ),address varchar ( 255 ) );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }
}
