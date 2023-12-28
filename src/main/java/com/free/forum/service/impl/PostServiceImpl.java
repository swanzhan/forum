package com.free.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.free.forum.beans.Comment;
import com.free.forum.beans.Post;
import com.free.forum.beans.UserInfo;
import com.free.forum.mapper.CommentMapper;
import com.free.forum.mapper.GroupMapper;
import com.free.forum.mapper.PostMapper;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.PostService;
import com.free.forum.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author swan
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final CommentMapper commentMapper;
    private final GroupMapper groupMapper;
    private final PostMapper postMapper;
    private final UserInfoMapper userInfoMapper;
    private final RedisTemplate<String, Object> redisTemplate;

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

    /**
     * 发布帖子
     *
     * @param post 帖子
     */
    @Override
    public void releasePost(Post post) {
        post.setId(UuidUtil.getShortUuid());
        post.setReleaseTime(new Date());
        post.setView(0);
        postMapper.insert(post);
    }

    /**
     * 删除帖子
     *
     * @param postId 帖子 ID
     * @return boolean
     */
    @Override
    public Boolean removePost(String postId) {
        return postMapper.deleteById(postId) > 0;
    }

    /**
     * 更新帖子
     *
     * @param post 发布
     * @return Post
     */
    @Override
    public Post updatePost(Post post) {
        postMapper.updateById(post);
        return postMapper.selectById(post.getId());
    }

    /**
     * 用户帖子
     *
     * @param pageNum 页码
     * @param userId  用户 ID
     * @param flag    类型
     * @return 页面信息
     */
    @Override
    public PageInfo<Post> userPosts(Integer pageNum, String userId, Integer flag) {
        PageHelper.startPage(pageNum, 4);
        List<Post> list = new ArrayList<>();
        if (flag == 0) {
            // 用户发布的帖子
            list = postMapper.selectList(new QueryWrapper<Post>().eq("userId", userId));
        } else if (flag == 1) {
            // 用户收藏的帖子
            list = userInfoMapper.findUserFavoritePostsById(userId);
        }
        for (Post post : list) {
            post.setUser(userInfoMapper.selectById(post.getUserId()));
            post.setGroup(groupMapper.selectById(post.getGroupId()));
        }
        return new PageInfo<>(list);
    }

    /**
     * 帖子详情
     *
     * @param postId 帖子 ID
     * @return 发布
     */
    @Override
    public Post details(String postId) {
        Post post = postMapper.selectById(postId);
        post.setUser(userInfoMapper.selectById(post.getUserId()));
        post.setGroup(groupMapper.selectById(post.getGroupId()));
        return post;
    }

    /**
     * 帖子浏览量
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     */
    @Override
    public void postView(String userId, String postId) {
        String key = "post" + postId;
        boolean isMember = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("postCache", postId));
        if (!isMember) {
            redisTemplate.opsForSet().add("postCache", postId);
        }
        redisTemplate.opsForSet().add(key, userId);
    }

    /**
     * 是否收藏
     *
     * @param postId 帖子 ID
     * @param userId 用户 ID
     * @return boolean
     */
    @Override
    public boolean isFavorite(String userId, String postId) {
        int count = userInfoMapper.findPostFavorite(userId, postId);
        return count != 0;
    }

    /**
     * 帖子收藏
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     */
    @Override
    public void favorite(String userId, String postId) {
        userInfoMapper.insertByUserIdAndPostId(userId, postId);
    }

    /**
     * 帖子取消收藏
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     */
    @Override
    public void cancelFavorite(String userId, String postId) {
        userInfoMapper.deleteByUserIdAndPostId(userId, postId);
    }

    /**
     * 热门帖子
     *
     * @return 列表
     */
    @Override
    public List<Post> hotPost() {
        return postMapper.selectList(new QueryWrapper<Post>()
                .orderByDesc("view")
                .last("limit 0, 5"));
    }

    /**
     * 关键词搜索帖子和评论
     *
     * @param keyword 关键词
     * @return 列表
     */
    @Override
    public List<Post> mainSearch(String keyword) {
    /*
    SELECT
	    *
    FROM
        post
    WHERE
        id IN ( SELECT DISTINCT postId FROM comment WHERE content LIKE '%测试%' )
        OR ( title LIKE '%测试%' OR content LIKE '%测试%' );
    * */
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();

        // 使用 or 条件连接 title 和 content 字段的模糊查询
        queryWrapper.like("title", keyword).or().like("content", keyword);

        List<String> postIdList = commentMapper.selectList(
                        new QueryWrapper<Comment>().select("DISTINCT postId").like("content", keyword))
                .stream()
                .map(Comment::getPostId)
                .collect(Collectors.toList());

        // 使用 or 条件连接 id 和 postId 字段的 in 查询
        queryWrapper.or().in("id", postIdList);
        List<Post> postList = postMapper.selectList(queryWrapper);

        List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>()
                .like("content", keyword)
                .last("limit 2"));
        return postList.stream()
                .peek(post -> post.setUser(userInfoMapper.selectById(post.getUserId())))
                .peek(post -> post.setGroup(groupMapper.selectById(post.getGroupId())))
                .peek(post -> post.setComments(commentList.stream()
                        .peek(comment -> comment.setUser(userInfoMapper.selectById(comment.getUserId())))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
