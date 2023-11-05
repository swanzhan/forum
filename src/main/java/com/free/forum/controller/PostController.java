package com.free.forum.controller;

import com.free.forum.beans.Post;
import com.free.forum.service.PostService;
import com.free.forum.utils.ResultInfo;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author swan
 */
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
    public PageInfo<Post> groupPosts(@RequestParam(defaultValue = "1") Integer pageNum, String groupId, String sortStatus) {
        return postService.groupPosts(pageNum, groupId, sortStatus);
    }

    /**
     * 组热门帖子
     *
     * @param groupId 组 ID
     * @return 列表
     */
    @RequestMapping("groupTrendPosts")
    public List<Post> groupTrendPosts(String groupId) {
        return postService.groupTrendPosts(groupId);
    }

    /**
     * 发布帖子
     *
     * @param post  帖子
     * @param files 文件
     * @return 结果信息
     * @throws IOException IO异常
     */
    @RequestMapping("releasePost")
    public ResultInfo releasePost(HttpServletRequest request, Post post, @RequestParam("pictureFile") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String extension = null;
            if (filename != null) {
                extension = filename.substring(filename.lastIndexOf("."));
            }
            filename = uuid + extension;
            String realPath = request.getServletContext().getRealPath("/img/post-img");
            File parent = new File(realPath);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            file.transferTo(new File(parent, filename));
            // 关联上传后的图片路径
            if (post.getPic() != null) {
                post.setPic(post.getPic() + "/" + filename);
            } else {
                post.setPic(filename);
            }
        }
        postService.releasePost(post);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(true);
        return resultInfo;
    }

    /**
     * 用户帖子
     *
     * @param pageNum 页码
     * @param userId  用户 ID
     * @param flag    分类
     * @return 页面信息
     */
    @RequestMapping("userPosts")
    public PageInfo<Post> userPosts(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam String userId, @RequestParam Integer flag) {
        return postService.userPosts(pageNum, userId, flag);
    }

    /**
     * 帖子详情
     *
     * @param id 编号
     * @return Post
     * @throws Exception 异常
     */
    @RequestMapping(value = "details/{id}", method = RequestMethod.GET)
    public Post details(@PathVariable String id) throws Exception {
        return postService.details(id);
    }

    /**
     * 帖子浏览量
     *
     * @param userId 用户 ID
     * @param postId 帖子 ID
     * @return 结果信息
     */
    @RequestMapping("postView")
    public ResultInfo postView(@RequestParam String userId, @RequestParam String postId) {
        postService.postView(userId, postId);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(true);
        return resultInfo;
    }

    /**
     * 是否收藏/喜欢
     *
     * @param postId 帖子 ID
     * @param userId 用户 ID
     * @return 结果信息
     */
    @RequestMapping("isFavorite")
    public ResultInfo isFavorite(@RequestParam String userId, @RequestParam String postId) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(postService.isFavorite(userId, postId));
        return resultInfo;
    }

    /**
     * 帖子收藏
     *
     * @param postId 帖子 ID
     * @param userId 用户 ID
     * @return 结果信息
     */
    @RequestMapping("favorite")
    public ResultInfo favorite(@RequestParam String userId, @RequestParam String postId) {
        postService.favorite(userId, postId);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(true);
        return resultInfo;
    }

    /**
     * 帖子取消收藏
     *
     * @param postId 帖子 ID
     * @param userId 用户 ID
     * @return 结果信息
     */
    @RequestMapping("cancelFavorite")
    public ResultInfo cancelFavorite(@RequestParam String userId, @RequestParam String postId) {
        postService.cancelFavorite(userId, postId);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(false);
        return resultInfo;
    }

    /**
     * 热门帖子
     *
     * @return 结果信息
     */
    @RequestMapping("hotPost")
    public ResultInfo hotPost(){
        List<Post> list = postService.hotPost();
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(list);
        resultInfo.setFlag(true);
        return resultInfo;
    }

    /**
     * 关键词搜索帖子和评论
     *
     * @param keyword 关键词
     * @return 列表
     */
    @RequestMapping("mainSearch")
    public List<Post> mainSearch(String keyword){
        return postService.mainSearch(keyword);
    }
}
