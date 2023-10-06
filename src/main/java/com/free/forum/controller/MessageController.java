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

    /**
     * 消息列表
     *
     * @param pageNum 页码
     * @param userId  用户 ID
     * @return 消息分页
     */
    @RequestMapping("conversationList")
    public PageInfo<Message> conversationList(@RequestParam(defaultValue = "1") Integer pageNum, String userId) {
        return messageService.conversationList(pageNum, userId);
    }

    /**
     * 聊天消息
     *
     * @param pageNum   页码
     * @param userId    用户 ID
     * @param partnerId 聊天对象 ID
     * @return 消息分页
     */
    @RequestMapping("conversation")
    public PageInfo<Message> conversation(@RequestParam(defaultValue = "1") Integer pageNum, String userId, String partnerId) {
        return messageService.conversation(pageNum, userId, partnerId);
    }

    /**
     * 发送消息
     *
     * @param message 消息内容
     * @return 结果信息
     */
    @RequestMapping("sendMessage")
    public ResultInfo sendMessage(@RequestBody Message message) {
        messageService.sendMessage(message);
        return new ResultInfo(true);
    }
}
