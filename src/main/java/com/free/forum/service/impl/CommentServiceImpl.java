package com.free.forum.service.impl;

import com.free.forum.mapper.CommentMapper;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final RedisTemplate redisTemplate;
    private final CommentMapper commentMapper;
    private final UserInfoMapper userInfoMapper;
}
