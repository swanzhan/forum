package com.free.forum.service.impl;

import com.free.forum.beans.Message;
import com.free.forum.beans.UserInfo;
import com.free.forum.mapper.MessageMapper;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.MessageService;
import com.free.forum.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author swan
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    private final UserInfoMapper userInfoMapper;

    /**
     * 消息列表
     *
     * @param pageNum 页码
     * @param userId  用户 ID
     * @return 页面信息<消息>
     */
    @Override
    public PageInfo<Message> conversationList(Integer pageNum, String userId) {
        PageHelper.startPage(pageNum, 15);
        List<Message> conversationList = messageMapper.findConversationList(userId);
        PageInfo<Message> pageInfo = new PageInfo<>(conversationList);
        for (Message conversation : conversationList) {
            String conversationId = conversation.getConversationId();
            // 封装未读消息数
            Integer unreadCount = messageMapper.findConversationUnreadCount(conversationId, userId);
            conversation.setUnreadCount(unreadCount);
            // 封装用户信息
            String partnerId = conversationId.replace(userId, "");
            UserInfo partner = userInfoMapper.selectById(partnerId);
            conversation.setPartner(partner);
            // 封装最新的一条消息
            Message message = messageMapper.findConversationByConversationId(conversationId);
            conversation.setContent(message.getContent());
        }
        return pageInfo;
    }

    /**
     * 聊天消息
     *
     * @param pageNum   页码
     * @param userId    用户 ID
     * @param partnerId 聊天对象 ID
     * @return 页面信息<消息>
     */
    @Override
    public PageInfo<Message> conversation(Integer pageNum, String userId, String partnerId) {
        String conversationId = getConversationId(userId, partnerId);
        PageHelper.startPage(pageNum, 7);
        // 根据 conversationId 查聊天消息
        List<Message> conversation = messageMapper.findConversation(conversationId);
        // 设置当前 conversation status = 1
        messageMapper.modifyMessageStatus(conversationId, userId);
        for (Message message : conversation) {
            // 封装用户信息
            UserInfo partner = userInfoMapper.selectById(conversationId.replace(userId, ""));
            message.setPartner(partner);
        }
        return new PageInfo<>(conversation);
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    @Override
    public void sendMessage(Message message) {
        message.setId(UuidUtil.getShortUuid());
        String conversationId = getConversationId(message.getSendId(), message.getReceiveId());
        message.setConversationId(conversationId);
        message.setTime(new Date());
        message.setStatus(0);
        messageMapper.insert(message);
    }

    /**
     * 获取对话 ID
     *
     * @param sendId    发送 ID
     * @param receiveId 接收 ID
     * @return conversationId 对话 ID
     */
    private String getConversationId(String sendId, String receiveId) {
        // 比较 sendId 和 receiveId, 将较小的放在前面
        String smallerId = sendId.compareTo(receiveId) < 0 ? sendId : receiveId;
        String largerId = sendId.compareTo(receiveId) < 0 ? receiveId : sendId;
        // 拼接较小的 id 和较大的 id, 以生成 conversationId
        return smallerId + largerId;
    }
}
