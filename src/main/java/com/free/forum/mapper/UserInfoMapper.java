package com.free.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.forum.beans.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户映射
 *
 * @author swanzhan
 * @date 2023/10/01
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
