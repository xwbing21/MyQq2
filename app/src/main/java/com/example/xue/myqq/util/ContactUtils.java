package com.example.xue.myqq.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.support.annotation.NonNull;
import com.example.xue.myqq.bean.Contact;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.Data;

/**
 * 获取联系人信息
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/17 17:58
 **/
public class ContactUtils {
    private static final String TAG = "ContactUtils";
    // 联系人列表
    private List<Contact> mContactList;
    // 上下文环境
    private Context mContext;

    public ContactUtils(@NonNull Context context) {
        mContactList = new ArrayList<>();
        this.mContext = context;
    }

    /**
     * 插入数据 姓名 电话号码
     *
     * @param contact Contact
     */
    public void insertContact(@NonNull Contact contact) {
        String lastName = contact.getName().substring(1);
        // 插入raw_contacts表，并获取_id属性
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        long contact_id = ContentUris.parseId(resolver.insert(uri, values));
        // 插入data表
        uri = Uri.parse("content://com.android.contacts/data");
        // add Name
        values.put("raw_contact_id", contact_id);
        values.put(Data.MIMETYPE, "vnd.android.cursor.item/name");
        values.put("data2", lastName);   // data2 ==> 名 data3 ==> 姓
        values.put("data1", contact.getName());  // 姓名
        resolver.insert(uri, values);
        values.clear();
        // add Phone
        values.put("raw_contact_id", contact_id);
        values.put(Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
        values.put("data2", "2");   // 代表插入的是手机
        values.put("data1", contact.getPhoneNum());    // 手机号码
        resolver.insert(uri, values);
        values.clear();
    }

    /**
     * 读取通讯录
     *
     * @return mContactList
     */
    public List<Contact> readAll() {
        //uri = content://com.android.contacts/contacts
        Uri uri = Uri.parse("content://com.android.contacts/contacts"); //访问raw_contacts表
        ContentResolver resolver = this.mContext.getContentResolver();
        //获得_id属性
        Cursor cursor = resolver.query(uri, new String[]{Data._ID}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // 获得id并且在data中寻找数据
                long id = cursor.getInt(0);
                uri = Uri.parse("content://com.android.contacts/contacts/" + id + "/data");
                // data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
                Cursor cursor2 = resolver.query(uri, new String[]{Data.DATA1, Data.MIMETYPE}, null, null, null);
                if (cursor2 != null) {
                    String name = null;
                    String phoneNum = null;
                    Bitmap portrait = null;
                    while (cursor2.moveToNext()) {
                        String data = cursor2.getString(cursor2.getColumnIndex("data1"));
                        switch (cursor2.getString(cursor2.getColumnIndex("mimetype"))) {
                            // 如果是名字
                            case "vnd.android.cursor.item/name":
                                name = data;
                                break;
                            // 如果是电话
                            case "vnd.android.cursor.item/phone_v2":
                                phoneNum = data;
                                break;
                            // 如果是email
                            // case "vnd.android.cursor.item/email_v2":
                            //
                            //     break;
                            // // 如果是地址
                            // case "vnd.android.cursor.item/postal-address_v2":
                            //
                            //     break;
                            // // 如果是组织
                            // case "vnd.android.cursor.item/organization":
                            //
                            //     break;
                            default:
                                break;
                        }
                    }
                    // 关闭数据库
                    cursor2.close();

                    // 获取头像
                    portrait = getPhoto(resolver, id);

                    String sortKey = Cn2PinYin.getFirstLetterPinYin(name).toUpperCase();

                    Contact contact = new Contact()
                            .setName(name)
                            .setPhoneNum(phoneNum)
                            .setPortrait(portrait)
                            .setSortKey(sortKey);
                    mContactList.add(contact);
                }
            }
            // 关闭数据库
            cursor.close();
        }
        return mContactList;
    }

    /**
     * 获取联系人的图片
     */
    private Bitmap getPhoto(final ContentResolver contentResolver, long contactId) {
        Bitmap photo = null;
        Cursor dataCursor = contentResolver.query(
                Data.CONTENT_URI,
                new String[]{"data15"},
                Data.CONTACT_ID + "=?" + " AND "
                        + Data.MIMETYPE + "='" + Photo.CONTENT_ITEM_TYPE + "'",
                new String[]{String.valueOf(contactId)}, null);

        if (dataCursor != null) {
            if (dataCursor.getCount() > 0) {
                dataCursor.moveToFirst();
                byte[] bytes = dataCursor.getBlob(dataCursor.getColumnIndex("data15"));
                if (bytes != null) {
                    photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
            }
            dataCursor.close();
        }
        return photo;
    }
}
