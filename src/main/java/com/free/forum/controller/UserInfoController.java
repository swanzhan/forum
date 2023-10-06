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
}
