package com.free.forum.controller;


import com.free.forum.beans.Message;
import com.free.forum.service.MessageService;
import com.free.forum.utils.ResultInfo;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping("conversationList")
    public PageInfo<Message> conversationList(@RequestParam(defaultValue = "1") Integer pageNum, String userId) {
        return messageService.conversationList(pageNum, userId);
    }

    @RequestMapping("conversation")
    public PageInfo<Message> conversation(@RequestParam(defaultValue = "1") Integer pageNum, String userId, String partnerId) {
        return messageService.conversation(pageNum, userId, partnerId);
    }

    @RequestMapping("sendMessage")
    public ResultInfo sendMessage(@RequestBody Message message) {
        messageService.sendMessage(message);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(true);
        return resultInfo;
    }
}
