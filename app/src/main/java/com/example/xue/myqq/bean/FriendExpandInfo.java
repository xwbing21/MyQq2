package com.example.xue.myqq.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * QQ好友列表数据
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/23 12:17
 **/
@Data
@Accessors(chain = true)
public class FriendExpandInfo {
    private String title;
    private List<FriendInfo> friendList;
}
