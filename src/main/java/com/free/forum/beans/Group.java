package com.free.forum.beans;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("`group`")
public class Group {
    private String id;
    private String name;
    // 简介
    private String brief;
    @TableField("createTime")
    // 局部处理时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;
    private String image;
    // 管理员 id
    private String admin;
    @TableField(exist = false)
    private UserInfo adminInfo;
    @TableField(exist = false)
    private Integer memberCount;
    @TableField(exist = false)
    private List<UserInfo> members;
    @TableField(exist = false)
    private List<Post> hotPosts;
}
