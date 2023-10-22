package com.free.forum.service;

import com.free.forum.beans.UserInfo;
import com.github.pagehelper.PageInfo;

public interface UserInfoService {
    UserInfo login(UserInfo userInfo) throws Exception;

    void register(UserInfo userInfo) throws Exception;

    PageInfo<UserInfo> userList(Integer pageNum, String userId);

    Integer memberCount();

    PageInfo<UserInfo> memberSearch(String keyword);

    void focusMember(String userId, String memberId, Integer type);

    UserInfo userDetail(String userId);
}
