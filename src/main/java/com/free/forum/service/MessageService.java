package com.free.forum.service;

import com.free.forum.beans.Message;
import com.github.pagehelper.PageInfo;

public interface MessageService {
    PageInfo<Message> conversationList(Integer pageNum, String userId);

    PageInfo<Message> conversation(Integer pageNum, String userId, String partnerId);

    void sendMessage(Message message);
}
