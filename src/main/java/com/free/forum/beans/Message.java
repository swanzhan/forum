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
    @TableField(value = "sendId")
    private String sendId;
    @TableField(value = "receiveId")
    private String receiveId;
    @TableField(value = "conversationId")
    private String conversationId;
    private Date time;
    private Integer status;
    @TableField(exist = false)
    private Integer unreadCount;
    @TableField(exist = false)
    private UserInfo partner;
}
