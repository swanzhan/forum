package com.free.forum.beans;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("message")
public class Message {
    @TableId
    private String id;
    private String content;
    // 发送方 id
    private String sendId;
    // 接收方 id
    private String receiveId;
    // 双方会话 id
    private String conversationId;
    private Date time;
    // 标识消息的阅读状态（0表示未读，1表示已读）
    private Integer status;
    @TableField(exist = false)
    private Integer unreadCount;
    @TableField(exist = false)
    private UserInfo partner;
}
