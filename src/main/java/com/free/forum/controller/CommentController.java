package com.free.forum.controller;

import com.free.forum.beans.Comment;
import com.free.forum.service.CommentService;
import com.free.forum.utils.ResultInfo;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author swan
 */
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * 评论列表
     *
     * @param pageNum 页码
     * @param postId  帖子 ID
     * @param userId  用户 ID
     * @return 列表
     */
    @RequestMapping("list")
    public List<PageInfo<Comment>> commentList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam String postId, @RequestParam String userId) {
        return commentService.commentList(pageNum, postId, userId);
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
    @RequestMapping("secondList")
    public PageInfo<Comment> secondCommentList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam String postId, @RequestParam String userId, @RequestParam String rootId) {
        return commentService.secondCommentList(pageNum, postId, userId, rootId);
    }

    /**
     * 发送评论
     *
     * @param comment 评论
     * @return 结果信息
     */
    @RequestMapping("send")
    public ResultInfo send(@RequestBody Comment comment) {
        commentService.send(comment);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(true);
        return resultInfo;
    }

    /**
     * 评论点赞
     *
     * @param commentId 评论 ID
     * @param userId    用户 ID
     * @return 结果信息
     */
    @RequestMapping("like")
    public ResultInfo like(@RequestParam String commentId, @RequestParam String userId) {
        commentService.like(commentId, userId);
        long likeCount = commentService.findLikeCount(commentId);
        boolean likeStatus = commentService.findLikeStatus(userId, commentId);
        //返回JSON格式的字符串
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(likeCount);
        resultInfo.setFlag(likeStatus);
        return resultInfo;
    }

    /**
     * 点赞数
     *
     * @param commentId 评论 ID
     * @param userId    用户 ID
     * @return 结果信息
     */
    @RequestMapping("likeCounts")
    public ResultInfo likeCounts(@RequestParam String commentId, @RequestParam String userId) {
        long likeCount = commentService.findLikeCount(commentId);
        boolean likeStatus = commentService.findLikeStatus(userId, commentId);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(likeCount);
        resultInfo.setFlag(likeStatus);
        return resultInfo;
    }
}
