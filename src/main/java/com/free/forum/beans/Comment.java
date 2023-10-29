package com.free.forum.beans;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author swan
 */
@Data
@TableName("comment")
public class Comment {
    private String id;
    private String content;
    @TableField("replyTime")
    private Date replyTime;
    @TableField("userId")
    private String userId;
    // 所属帖子 id
    @TableField("postId")
    private String postId;
    // 父级评论 id
    @TableField("parentId")
    private String parentId;
    // 顶级评论 id
    @TableField("rootId")
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
