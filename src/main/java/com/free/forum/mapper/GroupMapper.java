package com.free.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.forum.beans.Group;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    List<Group> findGroups();

    List<Group> findGroupsByKeyword(String keyword);

    /**
     * 按组 ID 查找成员总数
     *
     * @param groupId 组 ID
     * @return 整数
     */
    Integer findMemberCountByGroupId(String groupId);
}
