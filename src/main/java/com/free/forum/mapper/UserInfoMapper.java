package com.free.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.free.forum.beans.Group;
import com.free.forum.beans.Post;
import com.free.forum.beans.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author swan
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    Integer findMemberCountByGroupId(String groupId);

    Integer findFocusStatus(@Param("userId") String userId, @Param("friendId") String friendId);

    void insertByUserIdAndMemberId(@Param("userId") String userId, @Param("memberId") String memberId);

    void deleteByUserIdAndMemberId(@Param("userId") String userId, @Param("memberId") String memberId);

    int countUserGroupMembership(@Param("userId") String userId, @Param("groupId") String groupId);

    void insertByUserIdAndGroupId(@Param("userId") String userId, @Param("groupId") String groupId);

    void deleteByUserIdAndGroupId(@Param("userId") String userId, @Param("groupId") String groupId);

    List<UserInfo> findByGroupId(String groupId);

    List<UserInfo> findByGroupIdAndUserId(@Param("groupId") String groupId, @Param("userId") String userId);

    List<Post> findUserFavoritePostsById(String userId);

    List<Group> findMemberGroupsByUserId(String userId);

    List<UserInfo> findMemberFocusByUserId(String userId);

    List<UserInfo> findMemberFansByUserId(String userId);

    int findPostFavorite(@Param("userId") String userId, @Param("postId") String postId);

    void insertByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);

    void deleteByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);
}
