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
    private String brief;
    private Date createTime;
    private String image;
    private String admin;
    @TableField(exist = false)
    private Integer memberCount; // 小组人数
    @TableField(exist = false)
    private List<Member> members;
    @TableField(exist = false)
    private List<Post> hotPosts;
}
