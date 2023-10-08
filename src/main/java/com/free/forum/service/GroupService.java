package com.free.forum.service;

import com.free.forum.beans.Group;
import com.github.pagehelper.PageInfo;

public interface GroupService {
    PageInfo<Group> groupList(Integer pageNum);

    Integer groupCount();

    PageInfo<Group> groupSearch(String keyword);
}
