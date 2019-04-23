package com.example.xue.myqq.bean;

import android.graphics.Bitmap;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 联系人实体类
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/23 13:57
 **/
@Data
@Accessors(chain = true)
public class Contact implements Comparable<Contact> {
    // 联系人姓名
    private String name;
    // 联系人电话
    private String phoneNum;
    // 联系人头像
    private Bitmap portrait;
    // 排序字母
    private String sortKey;

    @Override
    public int compareTo(Contact contact) {
        if ("#".equals(sortKey) && !"#".equals(contact.sortKey)) {
            return 1;
        } else if (!"#".equals(sortKey) && "#".equals(contact.sortKey)) {
            return -1;
        } else {
            return sortKey.compareToIgnoreCase(contact.sortKey);
        }
    }
}
