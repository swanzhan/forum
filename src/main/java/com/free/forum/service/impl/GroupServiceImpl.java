package com.free.forum.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.free.forum.beans.Comment;
import com.free.forum.beans.Group;
import com.free.forum.beans.Post;
import com.free.forum.beans.UserInfo;
import com.free.forum.mapper.CommentMapper;
import com.free.forum.mapper.GroupMapper;
import com.free.forum.mapper.PostMapper;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.GroupService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author swan
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupMapper groupMapper;
    private final UserInfoMapper userInfoMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    /**
     * 组列表
     *
     * @param pageNum 页码
     * @return 页面信息
     */
    @Override
    public PageInfo<Group> groupList(Integer pageNum) {
        PageHelper.startPage(pageNum, 8);
        List<Group> list = groupMapper.selectList(null);
        for(Group group : list) {
            Integer memberCount = userInfoMapper.findMemberCountByGroupId(group.getId());
            group.setMemberCount(memberCount);
        }
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
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyword).or()
                .like("brief", keyword);
        List<Group> list = groupMapper.selectList(queryWrapper);
        for(Group group : list) {
            Integer memberCount = userInfoMapper.findMemberCountByGroupId(group.getId());
            group.setMemberCount(memberCount);
        }
        return new PageInfo<>(list);
    }

    /**
     * 组主页
     *
     * @param groupId 组 ID
     * @return group and memberCount
     */
    @Override
    public Group groupHomeByGroupId(String groupId) {
        Group group = groupMapper.selectOne(new QueryWrapper<Group>().eq("id", groupId));
        Integer memberCount = userInfoMapper.findMemberCountByGroupId(group.getId());
        group.setMemberCount(memberCount);
        group.setAdminInfo(userInfoMapper.selectById(group.getAdmin()));
        return group;
    }

    /**
     * 加入群组
     *
     * @param userId  用户 ID
     * @param groupId 组 ID
     * @param type    加入 or 退出
     * @return boolean
     */
    @Override
    public boolean joinGroup(String userId, String groupId, Integer type) {
        if (type == 0) {
            // 检查是否加入
            int count = userInfoMapper.countUserGroupMembership(userId, groupId);
            return count > 0;
        } else if (type == 1) {
            // 加入
            userInfoMapper.insertByUserIdAndGroupId(userId, groupId);
            return true;
        } else {
            // 退出
            userInfoMapper.deleteByUserIdAndGroupId(userId, groupId);
            return false;
        }
    }

    /**
     * 随机组
     *
     * @param groupId 组 ID
     * @return 列表
     */
    @Override
    public List<Group> groupRandom(String groupId) {
        Long count = groupMapper.selectCount(null);
        List<Group> list = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 3) {
            int r = (int) (Math.random() * count);
            set.add(r);
        }
        for (Integer i : set) {
            Group group = groupMapper.selectOne(new QueryWrapper<Group>().last("limit " + i + ", 1"));
            list.add(group);
        }
        for(Group group : list) {
            Integer memberCount = userInfoMapper.findMemberCountByGroupId(group.getId());
            group.setMemberCount(memberCount);
        }
        return list;
    }

    /**
     * 组成员列表
     *
     * @param pageNum 页码
     * @param groupId 组 ID
     * @param userId  用户 ID
     * @return 页面信息
     */
    @Override
    public PageInfo<UserInfo> memberList(Integer pageNum, String groupId, String userId) {
        PageHelper.startPage(pageNum, 8);
        List<UserInfo> list;
        if (userId == null) {
            list = userInfoMapper.findByGroupId(groupId);
        } else {
            list = userInfoMapper.findByGroupIdAndUserId(groupId, userId);
            for (UserInfo userInfo : list) {
                userInfo.setFocusFlag(userInfoMapper.findFocusStatus(userId, userInfo.getId()) == 1);
            }
        }
        return new PageInfo<>(list);
    }

    /**
     * 组活跃成员
     *
     * @param groupId 组 ID
     * @return 列表
     */
    @Override
    public List<UserInfo> activeGroupMember(String groupId) {
        // 指定要返回的活跃成员数量
        int count = 3;
        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<>();
        postQueryWrapper.select("userId")
                .groupBy("userId")
                .orderByDesc("COUNT(*)")
                .last("limit 0, " + count);
        List<String> userIdList = new ArrayList<>();
        List<Post> postList = postMapper.selectList(postQueryWrapper);
        postList.forEach(post -> userIdList.add(post.getUserId()));
        if (userIdList.isEmpty()) {
            return new ArrayList<>();
        }
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.in("id", userIdList);
        return userInfoMapper.selectList(userInfoQueryWrapper);
    }

    /**
     * 热门小组推荐
     *
     * @return 列表
     */
    @Override
    public List<Group> recommend() {
        List<String> groupIdList = postMapper.selectList(new QueryWrapper<Post>()
                        .select("groupId")
                        .groupBy("groupId")
                        .orderByDesc("COUNT(*)")
                        .last("limit 0, 3"))
                .stream()
                .map(Post::getGroupId)
                .collect(Collectors.toList());
        List<Group> groupList = groupMapper.selectBatchIds(groupIdList);
        for (Group group : groupList) {
            List<Post> postList = postMapper.selectList(new QueryWrapper<Post>()
                    .eq("groupId", group.getId())
                    .orderByDesc("view")
                    .last("limit 0, 2"));
            for (Post post : postList) {
                post.setUser(userInfoMapper.selectById(post.getUserId()));
                post.setComments(commentMapper.selectList(new QueryWrapper<Comment>()
                        .eq("postId", post.getId())));
            }
            group.setHotPosts(postList);
        }
        return groupList;
    }
}
