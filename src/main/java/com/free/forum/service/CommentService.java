package com.free.forum.service;

import com.free.forum.beans.Comment;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author swan
 */
public interface CommentService {
    Long findLikeCount(String commentId);

    boolean findLikeStatus(String userId, String commentId);

    List<PageInfo<Comment>> commentList(Integer pageNum, String postId, String userId);

    PageInfo<Comment> secondCommentList(Integer pageNum, String postId, String userId, String rootId);

    void like(String commentId, String userId);

    void send(Comment comment);
}
