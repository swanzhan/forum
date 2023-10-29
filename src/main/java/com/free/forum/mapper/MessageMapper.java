package com.free.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.forum.beans.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author swan
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> findConversationList(String userId);

    Integer findConversationUnreadCount(@Param("conversationId") String conversationId, @Param("receiveId") String receiveId);

    Message findConversationByConversationId(String conversationId);

    List<Message> findConversation(String conversationId);

    void modifyMessageStatus(@Param("conversationId") String conversationId, @Param("receiveId") String receiveId);
}