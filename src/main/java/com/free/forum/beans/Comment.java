package com.free.forum.beans;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("comment")
public class Comment {
    private String id;
    private String content;
    private Date replyTime;
    private String userId;
    // 所属帖子 id
    private String postId;
    // 父级评论 id
    private String parentId;
    // 顶级评论 id
    private String rootId;
    @TableField(exist = false)
    private Long likeCounts;
    @TableField(exist = false)
    private boolean likeFlag;
    @TableField(exist = false)
    private UserInfo user;
    @TableField(exist = false)
    private String parentUsername;
    @TableField(exist = false)
    private List<Comment> comments;
}
