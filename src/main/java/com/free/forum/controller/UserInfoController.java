package com.free.forum.controller;

import com.free.forum.beans.UserInfo;
import com.free.forum.exception.LoginException;
import com.free.forum.service.UserInfoService;
import com.free.forum.utils.ResultInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 注册
     *
     * @param userInfo 用户信息
     * @return 结果信息
     * @throws Exception 异常
     */
    @RequestMapping("register")
    public ResultInfo register(@Valid @RequestBody UserInfo userInfo, BindingResult bindingResult) throws Exception {
        ResultInfo resultInfo = new ResultInfo();
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors().get(0).getDefaultMessage());
            resultInfo.setErrorMsg(bindingResult.getAllErrors().get(0).getDefaultMessage());
            resultInfo.setFlag(false);
        } else {
            userInfoService.register(userInfo);
            resultInfo.setFlag(true);
        }
        return resultInfo;
    }
}
