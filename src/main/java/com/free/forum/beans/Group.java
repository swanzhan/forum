package com.free.forum.beans;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.lang.reflect.Member;
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
    private Date createTime;
    private String image;
    // 管理员 id
    private String admin;
    @TableField(exist = false)
    private Integer memberCount;
    @TableField(exist = false)
    private List<Member> members;
    @TableField(exist = false)
    private List<Post> hotPosts;
}
