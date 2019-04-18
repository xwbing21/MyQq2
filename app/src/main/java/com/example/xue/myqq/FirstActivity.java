package com.example.xue.myqq;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {
    public static final String USERURI = "content://com.example.xue.myqq.UserContentProviderprovider/user";
    private static final String TAG = "FirstActivityResolover";
    private Button add;
    private Button del;
    private Button update;
    private Button query;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        Context context=this;
        mContentResolver = context.getContentResolver();
        initView();
        initEvent();

    }

    public void initView() {
        add = findViewById(R.id.add);
        del = findViewById(R.id.del);
        update = findViewById(R.id.update);
        query = findViewById(R.id.query);
    }

    public void initEvent()

    {   final Uri uri = Uri.parse(USERURI);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("account","1111222333");
                values.put("phone","123456");
                Uri newUri = mContentResolver.insert(uri, values);
                Log.d(TAG, "add: Uri"+ newUri);

            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int delete = mContentResolver.delete(uri, "id = ?", new String[]{"2"});
                Log.d(TAG, "delete: 影响行数 "+ delete);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("account","1044269480");
                int update = mContentResolver.update(uri, values, "account = ?", new String[]{"1111222333"});
                Log.d(TAG, "update: +影响行数"+update);

            }
        });
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = mContentResolver.query(uri, null, "id = ?", new String[]{"1"}, null);
                if (cursor!= null){
                    while (cursor.moveToNext()){
                        String phone = cursor.getString(cursor.getColumnIndex("phone"));
                        Log.d(TAG, "query: "+phone);
                    }
                }

            }
        });

    }
}
