package com.free.forum.service;

import com.free.forum.beans.Post;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface PostService {
    PageInfo<Post> groupPosts(Integer pageNum, String groupId, String sortStatus);

    List<Post> groupTrendPosts(String groupId);

    void releasePost(Post post);

    PageInfo<Post> userPosts(Integer pageNum, String userId, Integer flag);
}
