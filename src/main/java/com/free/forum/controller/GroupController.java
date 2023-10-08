package com.free.forum.controller;

import com.free.forum.beans.Group;
import com.free.forum.service.GroupService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    /**
     * 组列表
     *
     * @param pageNum 页码
     * @return 页面信息
     */
    @RequestMapping("groupList")
    public PageInfo<Group> groupList(@RequestParam(defaultValue = "1") Integer pageNum) {
        return groupService.groupList(pageNum);
    }

    /**
     * 组总数
     *
     * @return 整数
     */
    @RequestMapping("groupCount")
    public Integer groupCount() {
        return groupService.groupCount();
    }

    /**
     * 组搜索
     *
     * @param keyword 关键词
     * @return 页面信息
     */
    @RequestMapping("groupSearch")
    public PageInfo<Group> groupSearch(String keyword) {
        return groupService.groupSearch(keyword);
    }
}
