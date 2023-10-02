package com.free.forum.controller;

import com.free.forum.beans.UserInfo;
import com.free.forum.exception.LoginException;
import com.free.forum.service.UserInfoService;
import com.free.forum.utils.ResultInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        ResultInfo resultInfo = new ResultInfo();
        try {
            UserInfo user = userInfoService.login(userInfo);
            resultInfo.setFlag(true);
            resultInfo.setData(user);
        } catch (LoginException e) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultInfo;
    }
}
