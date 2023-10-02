package com.free.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.free.forum.beans.UserInfo;
import com.free.forum.exception.LoginException;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.UserInfoService;
import com.free.forum.utils.Md5Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoMapper userInfoMapper;

    /**
     * 登录
     *
     * @param userInfo 用户信息
     * @return 用户信息
     * @throws Exception 例外
     */
    @Override
    public UserInfo login(UserInfo userInfo) throws Exception {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userInfo.getUsername());
        UserInfo user = userInfoMapper.selectOne(queryWrapper);
        // 给密码加密
        String password = Md5Util.encodeByMd5(userInfo.getPassword());
        if (user == null) {
            throw new LoginException("用户名不存在");
        } else if (password.equals(user.getPassword())) {
            return user;
        } else {
            throw new LoginException("用户名或密码错误");
        }
    }
}
