package com.free.forum.beans;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("users")
public class UserInfo {
    private String id;
    private String username;
    private String password;
    private Integer age;
    private String nickname;
    private String phone;
    private String ip;
    private String email;
    private String head_url;
    private String introduce;
    @TableField(exist = false)
    private List<Post> posts;
    @TableField(exist = false)
    private List<Group> groups;
    @TableField(exist = false)
    private boolean focusFlag;
}
