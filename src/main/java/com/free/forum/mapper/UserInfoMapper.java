package com.free.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.forum.beans.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
