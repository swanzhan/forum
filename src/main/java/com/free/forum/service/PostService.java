package com.free.forum.service;

import com.free.forum.beans.Post;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author swan
 */
public interface PostService {
    PageInfo<Post> groupPosts(Integer pageNum, String groupId, String sortStatus);

    List<Post> groupTrendPosts(String groupId);

    void releasePost(Post post);

    PageInfo<Post> userPosts(Integer pageNum, String userId, Integer flag);

    Post details(String postId);

    void postView(String userId, String postId);

    boolean isFavorite(String userId, String postId);

    void favorite(String userId, String postId);

    void cancelFavorite(String userId, String postId);

    List<Post> hotPost();

    List<Post> mainSearch(String keyword);
}
