package com.free.forum.controller;

import com.free.forum.beans.Post;
import com.free.forum.service.PostService;
import com.free.forum.utils.ResultInfo;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    /**
     * 发布帖子
     *
     * @param request
     * @param post    帖子
     * @param files   文件
     * @return 结果信息
     * @throws IOException IO异常
     */
    @RequestMapping("releasePost")
    public ResultInfo releasePost(HttpServletRequest request, Post post, @RequestParam("pictureFile") MultipartFile[] files) throws IOException {
        for(MultipartFile file:files){
            String filename = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String extension = filename.substring(filename.lastIndexOf("."));
            filename = uuid + extension;
            String realPath = request.getServletContext().getRealPath("/img/post-img");
            File parent = new File(realPath);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            file.transferTo(new File(parent, filename));
            // 关联上传后的图片路径
            if(post.getPic()!=null){
                post.setPic(post.getPic()+"/"+filename);
            }
            else{
                post.setPic(filename);
            }
        }
        postService.releasePost(post);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(true);
        return resultInfo;
    }
}
