package com.free.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.forum.beans.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * Message 消息映射
 *
 * @author swanzhan
 * @date 2023/10/03
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}