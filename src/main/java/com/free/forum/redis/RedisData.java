package com.free.forum.redis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.free.forum.beans.Post;
import com.free.forum.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisData {

    private final PostMapper postMapper;

    private final RedisTemplate redisTemplate;

//    @Scheduled(cron = "*/5 * * * * *")
    @Scheduled(cron = "0 */1 * * * *")
    public void save() {
        Set<String> postIds = redisTemplate.opsForSet().members("postCache");
        // 遍历帖子ID集合
        for (String postId : postIds) {
            // 获取当前帖子在缓存中的浏览次数
            Long cachedViews = redisTemplate.opsForSet().size("post" + postId);
            int cachedViewsCount = cachedViews.intValue();
            int databaseViews = postMapper.selectOne(new QueryWrapper<Post>()
                            .select("view")
                            .eq("id", postId))
                    .getView();
            Post post = postMapper.selectById(postId);
            post.setView(databaseViews + cachedViewsCount);
            postMapper.updateById(post);
            // 从缓存中移除当前帖子ID
            redisTemplate.opsForSet().remove("postCache", postId);
            // 获取当前帖子在缓存中的所有评论ID集合
            Set<String> postCommentIds = redisTemplate.opsForSet().members("post" + postId);
            for (String commentId : postCommentIds) {
                redisTemplate.opsForSet().remove("post" + postId, commentId);
            }
        }
    }
}
