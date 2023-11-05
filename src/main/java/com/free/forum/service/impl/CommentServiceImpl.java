package com.free.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.free.forum.beans.Comment;
import com.free.forum.beans.UserInfo;
import com.free.forum.mapper.CommentMapper;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.CommentService;
import com.free.forum.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author swan
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final CommentMapper commentMapper;
    private final UserInfoMapper userInfoMapper;

    /**
     * 查找点赞数量
     *
     * @param commentId 评论 ID
     * @return 长
     */
    @Override
    public Long findLikeCount(String commentId) {
        String key = "comment" + commentId;
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 查找点赞状态
     *
     * @param userId    用户 ID
     * @param commentId 评论 ID
     * @return boolean
     */
    @Override
    public boolean findLikeStatus(String userId, String commentId) {
        String key = "comment" + commentId;
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId));
    }

    /**
     * 评论列表
     *
     * @param pageNum 页码
     * @param postId  帖子 ID
     * @param userId  用户 ID
     * @return 列表
     */
    @Override
    public List<PageInfo<Comment>> commentList(Integer pageNum, String postId, String userId) {
        PageHelper.startPage(pageNum, 5);
        List<PageInfo<Comment>> pageInfoList = new ArrayList<>();
        List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>()
                .eq("postId", postId)
                .isNull("parentId")
                .orderByDesc("replyTime"));
        for (Comment comment : commentList) {
            // 设置用户
            comment.setUser(userInfoMapper.selectById(comment.getUserId()));
            // 设置子评论
            comment.setComments(commentMapper.selectList(new QueryWrapper<Comment>()
                    .eq("rootId", comment.getId())));
            // 设置点赞数量
            comment.setLikeCounts(findLikeCount(comment.getId()));
            // 设置点赞状态
            comment.setLikeFlag(findLikeStatus(userId, comment.getId()));

            PageHelper.startPage(1, 2);
            List<Comment> secondCommentList = commentMapper.selectList(new QueryWrapper<Comment>()
                    .eq("postId", postId)
                    .eq("rootId", comment.getId())
                    .orderByDesc("replyTime"));
            for (Comment secondComment : secondCommentList) {
                // 设置用户
                secondComment.setUser(userInfoMapper.selectById(secondComment.getUserId()));
                secondComment.setLikeFlag(findLikeStatus(userId, secondComment.getId()));
                secondComment.setLikeCounts(findLikeCount(secondComment.getId()));
                String secondUserId = commentMapper.selectOne(new QueryWrapper<Comment>()
                                .select("userId")
                                .eq("id", secondComment.getParentId()))
                        .getUserId();
                String username = userInfoMapper.selectOne(new QueryWrapper<UserInfo>()
                        .select("username")
                        .eq("id", secondUserId)).getUsername();
                secondComment.setParentUsername(username);
            }
            pageInfoList.add(new PageInfo<>(secondCommentList));
        }
        pageInfoList.add(new PageInfo<>(commentList));
        return pageInfoList;
    }

    /**
     * 二级评论列表
     *
     * @param pageNum 页码
     * @param postId  帖子 ID
     * @param userId  用户 ID
     * @param rootId  根评论 ID
     * @return 页面信息
     */
    @Override
    public PageInfo<Comment> secondCommentList(Integer pageNum, String postId, String userId, String rootId) {
        PageHelper.startPage(pageNum, 2);
        List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>()
                .eq("postId", postId)
                .eq("rootId", rootId)
                .orderByDesc("replyTime"));
        for (Comment comment : commentList) {
            comment.setUser(userInfoMapper.selectById(comment.getUserId()));
            comment.setLikeFlag(findLikeStatus(userId, comment.getId()));
            comment.setLikeCounts(findLikeCount(comment.getId()));
            comment.setParentUsername(userInfoMapper.selectOne(new QueryWrapper<UserInfo>()
                   .select("username")
                   .eq("id", commentMapper.selectOne(new QueryWrapper<Comment>()
                           .select("userId")
                           .eq("id", comment.getParentId()))
                           .getUserId())).getUsername());
        }
        return new PageInfo<>(commentList);
    }

    /**
     * 评论点赞
     *
     * @param commentId 评论 ID
     * @param userId    用户 ID
     */
    @Override
    public void like(String commentId, String userId) {
        String key = "comment" + commentId;
        boolean isMember = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId));
        if (isMember) {
            redisTemplate.opsForSet().remove(key, userId);
        } else {
            redisTemplate.opsForSet().add(key, userId);
        }
    }

    /**
     * 发送评论
     *
     * @param comment 评论
     */
    @Override
    public void send(Comment comment) {
        comment.setId(UuidUtil.getShortUuid());
        comment.setReplyTime(new Date());
        commentMapper.insert(comment);
    }
}
