package com.free.forum;

import com.free.forum.beans.Post;
import com.free.forum.beans.UserInfo;
import com.free.forum.mapper.PostMapper;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class ForumApplicationTests {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PostService postService;

    @Test
    void contextLoads() {

    }

    @Test
    void testRedis() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        List<Post> postList = postMapper.selectList(null);
        List<String> postIds = postList.stream().map(Post::getId).collect(Collectors.toList());
        List<UserInfo> userInfos = userInfoMapper.selectList(null);
        List<String> userIds = userInfos.stream().map(UserInfo::getId).collect(Collectors.toList());
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            pool.execute(() -> {
                for (int j = 0; j < 5000; j++) {
                    postService.postView(userIds.get(random.nextInt(userIds.size())),
                            postIds.get(random.nextInt(postIds.size())));
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);
    }
    @Test
    void testSQL() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        List<Post> postList = postMapper.selectList(null);
        List<String> postIds = postList.stream().map(Post::getId).collect(Collectors.toList());
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            pool.execute(() -> {
                for (int j = 0; j < 5000; j++) {
                    Post post = postMapper.selectById(postIds.get(random.nextInt(postIds.size())));
                    post.setView(post.getView() + 1);
                    postMapper.updateById(post);
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);
    }

    @Test
    void name() {
        postMapper.deleteById("0uJHrP5a");
    }
}
