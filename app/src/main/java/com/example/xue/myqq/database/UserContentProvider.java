package com.example.xue.myqq.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.xue.myqq.util.UserUtil;

public class UserContentProvider extends ContentProvider {
    public static final int USER_DIR = 1;
    public static final int USER_ITEM = 2;
    public static final int FRIEND_DIR = 3;
    public static final int FRIEND_ITEM = 4;
    private static final String DB_NAME = "qqq.db";
    private static final String DB_TABLE = "userInfo";
    private static final String DB_TABLE2 = "friendInfo";
    private static UriMatcher uriMatcher;
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase db;

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(UserUtil.AUTHORITY, "user", USER_DIR);
        uriMatcher.addURI(UserUtil.AUTHORITY, "user/#", USER_ITEM);
        uriMatcher.addURI(UserUtil.AUTHORITY, "friend", FRIEND_DIR);
        uriMatcher.addURI(UserUtil.AUTHORITY, "friend/#", FRIEND_ITEM);
    }


    @Override
    public boolean onCreate() {
        myOpenHelper = new MyOpenHelper(getContext(), DB_NAME, null, 3);
        db = myOpenHelper.getWritableDatabase();
        if (db == null) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteRow = 0;
        switch (uriMatcher.match(uri)) {
            case USER_DIR: {
                deleteRow = db.delete(DB_TABLE, selection, selectionArgs);
                break;
            }
            case USER_ITEM: {
                //getPathSegments会根据URI权限后的/划分开
                String userId = uri.getPathSegments().get(1);
                deleteRow = db.delete(DB_TABLE, "id = ?", new String[]{userId});
                break;
            }
            case FRIEND_DIR: {
                deleteRow = db.delete(DB_TABLE2, selection, selectionArgs);
                break;
            }
            case FRIEND_ITEM: {
                //getPathSegments会根据URI权限后的/划分开
                String userId = uri.getPathSegments().get(1);
                deleteRow = db.delete(DB_TABLE2, "id = ?", new String[]{userId});
                break;
            }
            default: {
                break;
            }
        }
        return deleteRow;
    }


    //获取uri对象对应的MIME类型
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case USER_DIR: {
                return "vnd.android.cursor.dir/vnd.com.example.xue.myqq.provider.User";
            }
            case USER_ITEM: {
                return "vnd.android.cursor.item/vnd.com.example.xue.myqq.provider.User";
            }
            default: {
                break;
            }
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri uriReturn = null;

        switch (uriMatcher.match(uri)) {
            case USER_DIR:
            case USER_ITEM: {
                long id = db.insert(DB_TABLE, null, values);
                uriReturn = Uri.parse("Content://com.example.xue.myqq.provider/User/" + id);
                break;
            }
            case FRIEND_DIR:
            case FRIEND_ITEM: {
                long id = db.insert(DB_TABLE2, null, values);//nullColumnHack： 当values参数为空或者里面没有内容的时候，我们insert是会失败的（底层数据库不允许插入一个空行），为了防止这种情况，我们要在这里指定一个 列名，到时候如果发现将要插入的行为空行时，就会将你指定的这个列名的值设为null，然后再向数据库中插入。
                uriReturn = Uri.parse("Content://com.example.xue.myqq.provider/Friend/" + id);
                break;
            }
            default: {
                break;
            }
        }
        return uriReturn;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case USER_DIR: {
                cursor = db.query(DB_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case USER_ITEM: {
                String userId = uri.getPathSegments().get(1);
                cursor = db.query(DB_TABLE, projection, "id = ?", new String[]{userId}, null, null, sortOrder);
                break;
            }
            case FRIEND_DIR: {
                cursor = db.query(DB_TABLE2, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case FRIEND_ITEM: {
                String userId = uri.getPathSegments().get(1);
                cursor = db.query(DB_TABLE2, projection, "id = ?", new String[]{userId}, null, null, sortOrder);
                break;
            }
            default: {
                break;
            }
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateRow = 0;
        switch (uriMatcher.match(uri)) {
            case USER_DIR: {
                updateRow = db.update(DB_TABLE, values, selection, selectionArgs);
                break;
            }
            case USER_ITEM: {
                String userId = uri.getPathSegments().get(1);
                updateRow = db.update(DB_TABLE, values, "id = ?", new String[]{userId});
                break;
            }
            case FRIEND_DIR: {
                updateRow = db.update(DB_TABLE2, values, selection, selectionArgs);
                break;
            }
            case FRIEND_ITEM: {
                String userId = uri.getPathSegments().get(1);
                updateRow = db.update(DB_TABLE2, values, "id = ?", new String[]{userId});
                break;
            }
            default: {
                break;
            }
        }
        return updateRow;
    }
}
