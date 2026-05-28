package com.laoliu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laoliu.system.entity.User;
import com.laoliu.system.vo.response.UserResponse;

import java.util.List;
import java.util.Map;

/**
* @author 25516
* @description 针对表【user】的数据库操作Mapper
* @createDate 2026-03-16 10:31:02
* @Entity com.laoliu.system.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    String getRoleByUserId(Long userId);

    void updateRoleToCommonUser(Long userId);

    void updateRoleToAdmin(Long userId);

    List<Map<String, Object>> getAllBookings(Long userId);

    String getEncodePasswordByEmail(String email);

    Long getUserIdByEmail(String email);

    List<UserResponse> getAllUsers();

    void updatePasswordByEmail(String email, String password);
}
