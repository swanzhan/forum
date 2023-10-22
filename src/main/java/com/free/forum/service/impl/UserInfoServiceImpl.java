package com.free.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.free.forum.beans.Message;
import com.free.forum.beans.Post;
import com.free.forum.beans.UserInfo;
import com.free.forum.exception.LoginException;
import com.free.forum.mapper.MessageMapper;
import com.free.forum.mapper.PostMapper;
import com.free.forum.mapper.UserInfoMapper;
import com.free.forum.service.UserInfoService;
import com.free.forum.utils.Md5Util;
import com.free.forum.utils.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoMapper userInfoMapper;
    private final MessageMapper messageMapper;
    private final PostMapper postMapper;

    /**
     * 登录
     *
     * @param userInfo 用户信息
     * @return 用户信息
     * @throws Exception encodeByMd5
     */
    @Override
    public UserInfo login(UserInfo userInfo) throws Exception {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userInfo.getUsername());
        UserInfo user = userInfoMapper.selectOne(queryWrapper);
        // 密码加密
        String password = Md5Util.encodeByMd5(userInfo.getPassword());
        if (user == null) {
            throw new LoginException("用户名不存在");
        } else if (password.equals(user.getPassword())) {
            // 未读消息数
            Long messageCount = messageMapper.selectCount(new QueryWrapper<Message>()
                    .eq("receiveId", user.getId())
                    .eq("status", 0));
            user.setMessageCount(Math.toIntExact(messageCount));
            return user;
        } else {
            throw new LoginException("用户名或密码错误");
        }
    }


    /**
     * 注册
     *
     * @param userInfo 用户信息
     * @throws Exception encodeByMd5
     */
    @Override
    public void register(UserInfo userInfo) throws Exception {
        userInfo.setId(UuidUtil.getShortUuid());
        String password = Md5Util.encodeByMd5(userInfo.getPassword());
        userInfo.setPassword(password);
        userInfoMapper.insert(userInfo);
    }

    /**
     * 用户列表
     *
     * @param pageNum 页码
     * @param userId  用户 ID
     * @return 页面信息
     */
    @Override
    public PageInfo<UserInfo> userList(Integer pageNum, String userId) {
        if (userId == null) {
            PageHelper.startPage(pageNum, 8);
            List<UserInfo> list = userInfoMapper.selectList(null);
            return new PageInfo<>(list);
        } else {
            PageHelper.startPage(pageNum, 8);
            List<UserInfo> list = userInfoMapper.selectList(new QueryWrapper<UserInfo>().ne("id", userId));
            for (UserInfo userInfo : list) {
                userInfo.setFocusFlag(userInfoMapper.findFocusStatus(userId, userInfo.getId()) == 1);
            }
            return new PageInfo<>(list);
        }
    }

    /**
     * 成员计数
     *
     * @return 整数
     */
    @Override
    public Integer memberCount() {
        return Math.toIntExact(userInfoMapper.selectCount(null));
    }

    /**
     * 成员搜索
     *
     * @param keyword 关键词
     * @return 页面信息
     */
    @Override
    public PageInfo<UserInfo> memberSearch(String keyword) {
        PageHelper.startPage(1,8);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", keyword).or()
                    .like("nickname", keyword).or()
                    .like("ip", keyword).or()
                    .like("introduce", keyword);
        List<UserInfo> list = userInfoMapper.selectList(queryWrapper);
        return new PageInfo<>(list);
    }

    /**
     * 成员关注(单向)
     *
     * @param userId   用户 ID
     * @param memberId 被关注人 ID
     * @param type     关注 OR 取关
     */
    @Override
    public void focusMember(String userId, String memberId, Integer type) {
        if (type == 0) {
            userInfoMapper.insertByUserIdAndMemberId(userId, memberId);
        } else {
            userInfoMapper.deleteByUserIdAndMemberId(userId, memberId);
        }
    }

    /**
     * 用户详细信息
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    @Override
    public UserInfo userDetail(String userId) {
        UserInfo user = userInfoMapper.selectById(userId);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Post> postList = postMapper.selectList(queryWrapper);
        user.setPosts(postList);
        return user;
    }
}
