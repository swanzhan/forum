package com.free.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.free.forum.beans.Group;
import com.free.forum.beans.Post;
import com.free.forum.beans.UserInfo;
import com.free.forum.mapper.CommentMapper;
import com.free.forum.mapper.PostMapper;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.PostService;
import com.free.forum.utils.ResultInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final RedisTemplate redisTemplate;
    private final PostMapper postMapper;
    private final UserInfoMapper userInfoMapper;
    private final CommentMapper commentMapper;

    /**
     * 小组帖子
     *
     * @param pageNum    页码
     * @param groupId    组 ID
     * @param sortStatus 排序状态
     * @return 页面信息
     */
    @Override
    public PageInfo<Post> groupPosts(Integer pageNum, String groupId, String sortStatus) {
        PageHelper.startPage(pageNum, 4);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("groupId", groupId);
        if (sortStatus.equals("hottest")) {
            queryWrapper.orderByDesc("view");
        } else {
            queryWrapper.orderByDesc("releaseTime");
        }
        List<Post> list = postMapper.selectList(queryWrapper);
        for (Post post : list) {
            UserInfo user = userInfoMapper.selectById(post.getUserId());
            post.setUser(user);
        }
        return new PageInfo<>(list);
    }

    /**
     * 组热门帖子
     *
     * @param groupId 组 ID
     * @return 热门帖子列表
     */
    @Override
    public List<Post> groupTrendPosts(String groupId) {
        // 热门帖子的数量
        int count = 3;
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("groupId", groupId)
                .orderByDesc("view")
                .last("limit 0, " + count);
        return postMapper.selectList(queryWrapper);
    }


}
