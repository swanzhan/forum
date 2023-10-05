package com.free.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.forum.beans.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Message 消息映射
 *
 * @author swanzhan
 * @date 2023/10/03
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> selectConversationList(String userId);

    Integer selectConversationUnreadCount(@Param("conversationId") String conversationId, @Param("receiveId") String receiveId);

    Message selectConversationByConversationId(String conversationId);

    List<Message> selectConversation(String conversationId);

    void updateMessageStatus(@Param("conversationId") String conversationId, @Param("receiveId") String receiveId);
}