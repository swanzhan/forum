package com.free.forum.controller;

import com.free.forum.beans.Group;
import com.free.forum.beans.UserInfo;
import com.free.forum.exception.LoginException;
import com.free.forum.service.UserInfoService;
import com.free.forum.utils.ResultInfo;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("userInfo")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;

    /**
     * 登录
     *
     * @param userInfo 用户信息
     * @return 结果信息
     */
    @RequestMapping("login")
    public ResultInfo login(@RequestBody UserInfo userInfo) {
        ResultInfo resultInfo;
        try {
            UserInfo user = userInfoService.login(userInfo);
            resultInfo = new ResultInfo(true);
            resultInfo.setData(user);
        } catch (LoginException e) {
            resultInfo = new ResultInfo(false, e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultInfo;
    }

    /**
     * 注册
     *
     * @param userInfo 用户信息
     * @return 结果信息
     * @throws Exception encodeByMd5
     */
    @RequestMapping("register")
    public ResultInfo register(@Valid @RequestBody UserInfo userInfo, BindingResult bindingResult) throws Exception {
        ResultInfo resultInfo;
        if (bindingResult.hasErrors()) {
            resultInfo = new ResultInfo(false,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            userInfoService.register(userInfo);
            resultInfo = new ResultInfo(true);
        }
        return resultInfo;
    }

    /**
     * 用户列表
     *
     * @param pageNum 页码
     * @param userId  用户 ID
     * @return 页面信息
     */
    @RequestMapping("userList")
    public PageInfo<UserInfo> userList(@RequestParam(defaultValue = "1") Integer pageNum, String userId) {
        return userInfoService.userList(pageNum, userId);
    }

    /**
     * 成员数量
     *
     * @return 整数
     */
    @RequestMapping("memberCount")
    public Integer memberCount() {
        return userInfoService.memberCount();
    }

    /**
     * 成员搜索
     *
     * @param keyword 关键词
     * @return 页面信息
     */
    @RequestMapping("memberSearch")
    public PageInfo<UserInfo> memberSearch(String keyword) {
        return userInfoService.memberSearch(keyword);
    }

    /**
     * 成员关注(单向)
     *
     * @param userId   用户 ID
     * @param memberId 被关注人 ID
     * @param type     关注 OR 取关
     * @return 结果信息
     */
    @RequestMapping("focusMember")
    public ResultInfo focusMember(String userId, String memberId, Integer type) {
        userInfoService.focusMember(userId, memberId, type);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(true);
        return resultInfo;
    }

    /**
     * 用户详细信息
     *
     * @param userId 编号
     * @return 用户信息
     * @throws Exception 异常
     */
    @RequestMapping(value = "userDetail/{userId}",method = RequestMethod.GET)
    public UserInfo userDetail(@PathVariable String userId) throws Exception {
        return userInfoService.userDetail(userId);
    }

    /**
     * 修改个人资料
     *
     * @param userInfo 用户信息
     * @return 结果信息
     */
    @RequestMapping("change")
    public ResultInfo change(@RequestBody UserInfo userInfo){
        UserInfo user = userInfoService.change(userInfo);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(user);
        resultInfo.setFlag(true);
        return resultInfo;
    }

    /**
     * 成员好友
     *
     * @param pageNum    页码
     * @param userId     用户 ID
     * @param friendType 好友类型
     * @return 页面信息
     */
    @RequestMapping("memberFriends")
    public PageInfo<UserInfo> memberFriends(@RequestParam(defaultValue = "1") Integer pageNum, String userId, boolean friendType) {
        return userInfoService.memberFriends(pageNum, userId, friendType);
    }

    /**
     * 成员组
     *
     * @param pageNum 页码
     * @param userId  用户 ID
     * @return 页面信息
     */
    @RequestMapping("memberGroups")
    public PageInfo<Group> memberGroups(@RequestParam(defaultValue = "1") Integer pageNum, String userId) {
        return userInfoService.memberGroups(pageNum, userId);
    }
}
