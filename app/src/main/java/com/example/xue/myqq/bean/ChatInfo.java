package com.example.xue.myqq.bean;

import android.graphics.Bitmap;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 聊天消息实体类
 *
 * @author Sidney Ding
 * @version 1.0
 * @date 2019/4/23 13:36
 **/
@Data
@Accessors(chain = true)
public class ChatInfo {
    private int id;
    private String nickname;
    private Bitmap portrait;
    private String chatMessage;
    // 是否为本人发送
    private boolean isMeSend;
}
