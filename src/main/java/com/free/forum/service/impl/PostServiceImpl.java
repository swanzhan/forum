package com.free.forum.service.impl;

import com.free.forum.mapper.CommentMapper;
import com.free.forum.mapper.PostMapper;
import com.free.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final RedisTemplate redisTemplate;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
}
