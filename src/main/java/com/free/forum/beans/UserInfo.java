package com.free.forum.beans;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@TableName("users")
public class UserInfo {
    private String id;
    @NotBlank(message = "用户名不能为空")
    @Length(message = "用户名不能超过 {max} 个字符", max = 20)
    private String username;
    @NotBlank(message = "密码不能为空")
    @Length(message = "密码长度为 {min} 到 {max} 个字符", min = 8, max = 16)
    private String password;
    private Integer age;
    private String nickname;
    private String phone;
    private String ip;
    private String email;
    // 头像地址
    private String headUrl;
    // 个人介绍
    private String introduce;
    @TableField(exist = false)
    private Integer messageCount;
    @TableField(exist = false)
    private List<Post> posts;
    @TableField(exist = false)
    private List<Group> groups;
    @TableField(exist = false)
    private boolean focusFlag;
}
