package com.free.forum.controller;

import com.free.forum.beans.Post;
import com.free.forum.service.PostService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * 小组获取帖子
     *
     * @param pageNum    页码
     * @param groupId    组 ID
     * @param sortStatus 排序状态
     * @return 页面信息
     */
    @RequestMapping("groupPosts")
    public PageInfo<Post> groupPosts(@RequestParam(defaultValue = "1") Integer pageNum, String groupId, String sortStatus){
        return postService.groupPosts(pageNum, groupId, sortStatus);
    }


    /**
     * 组热门帖子
     *
     * @param groupId 组 ID
     * @return 列表
     */
    @RequestMapping("groupTrendPosts")
    public List<Post> groupTrendPosts(String groupId){
        return postService.groupTrendPosts(groupId);
    }
}
