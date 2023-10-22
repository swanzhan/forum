package com.free.forum.controller;

import com.free.forum.beans.Group;
import com.free.forum.beans.UserInfo;
import com.free.forum.service.GroupService;
import com.free.forum.utils.ResultInfo;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 组主页
     *
     * @param groupId 组 ID
     * @return 结果信息
     */
    @RequestMapping("groupHome")
    public ResultInfo groupHome(@RequestParam String groupId) {
        ResultInfo resultInfo = new ResultInfo();
        Group group = groupService.groupHomeByGroupId(groupId);
        resultInfo.setFlag(true);
        resultInfo.setData(group);
        return resultInfo;
    }

    /**
     * 加入组
     *
     * @param userId  用户 ID
     * @param groupId 组 ID
     * @param type    类型
     * @return 结果信息
     */
    @RequestMapping("joinGroup")
    public ResultInfo joinGroup(String userId, String groupId, Integer type) {
        boolean flag = groupService.joinGroup(userId, groupId, type);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(flag);
        return resultInfo;
    }

    /**
     * 随机组
     *
     * @param groupId 组 ID
     * @return 结果信息
     */
    @RequestMapping("groupRandom")
    public ResultInfo groupRandom(@RequestParam("groupId") String groupId) {
        List<Group> list = groupService.groupRandom(groupId);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(true);
        resultInfo.setData(list);
        return resultInfo;
    }

    /**
     * 组成员列表
     *
     * @param pageNum 页码
     * @param groupId 组 ID
     * @param userId  用户 ID
     * @return 页面信息
     */
    @RequestMapping("memberList")
    public PageInfo<UserInfo> memberList(@RequestParam(defaultValue = "1") Integer pageNum, String groupId, String userId) {
        return groupService.memberList(pageNum, groupId, userId);
    }

    /**
     * 组活跃成员
     *
     * @param groupId 组 ID
     * @return 列表
     */
    @RequestMapping("activeGroupMember")
    public List<UserInfo> activeGroupMember(String groupId) {
        return groupService.activeGroupMember(groupId);
    }
}
