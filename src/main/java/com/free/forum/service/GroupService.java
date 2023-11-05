package com.free.forum.service;

import com.free.forum.beans.Group;
import com.free.forum.beans.UserInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author swan
 */
public interface GroupService {
    PageInfo<Group> groupList(Integer pageNum);

    Integer groupCount();

    PageInfo<Group> groupSearch(String keyword);

    Group groupHomeByGroupId(String groupId);

    boolean joinGroup(String userId, String groupId, Integer type);

    List<Group> groupRandom(String groupId);

    PageInfo<UserInfo> memberList(Integer pageNum, String groupId, String userId);

    List<UserInfo> activeGroupMember(String groupId);

    List<Group> recommend();
}
