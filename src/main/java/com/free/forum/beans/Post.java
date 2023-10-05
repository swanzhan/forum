package com.free.forum.beans;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("post")
public class Post {
    private String id;
    private String title;
    private String content;
    // 发布时间
    private Date releaseTime;
    // 浏览量
    private Integer view;
    private String userId;
    private String groupId;
    private String pic;
    @TableField(exist = false)
    private UserInfo user;
    @TableField(exist = false)
    private Group group;
    @TableField(exist = false)
    private List<Comment> comments;
}
