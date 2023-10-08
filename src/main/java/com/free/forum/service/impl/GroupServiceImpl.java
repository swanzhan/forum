package com.free.forum.service.impl;


import com.free.forum.beans.Group;
import com.free.forum.mapper.GroupMapper;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.GroupService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupMapper groupMapper;
    private final UserInfoMapper userInfoMapper;

    /**
     * 组列表
     *
     * @param pageNum 页码
     * @return 页面信息
     */
    @Override
    public PageInfo<Group> groupList(Integer pageNum) {
        PageHelper.startPage(pageNum, 8);
        List<Group> list = groupMapper.findGroups();
        return new PageInfo<>(list);
    }

    /**
     * 组总数
     *
     * @return 整数
     */
    @Override
    public Integer groupCount() {
        return Math.toIntExact(groupMapper.selectCount(null));
    }

    /**
     * 组搜索
     *
     * @param keyword 关键词
     * @return 页面信息
     */
    @Override
    public PageInfo<Group> groupSearch(String keyword) {
        PageHelper.startPage(1, 8);
        List<Group> list = groupMapper.findGroupsByKeyword(keyword);
        return new PageInfo<>(list);
    }
}
