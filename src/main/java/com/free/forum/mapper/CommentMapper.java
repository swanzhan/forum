package com.free.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.forum.beans.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
