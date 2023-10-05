package com.free.forum.service;

import com.free.forum.beans.UserInfo;

public interface UserInfoService {
    UserInfo login(UserInfo userInfo) throws Exception;

    void register(UserInfo userInfo) throws Exception;
}
