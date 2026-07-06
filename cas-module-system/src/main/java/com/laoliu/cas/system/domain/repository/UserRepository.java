package com.laoliu.cas.system.domain.repository;

import com.laoliu.cas.system.domain.entity.User;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户领域仓库接口
 *
 * @author forever-king
 */
public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User save(User user);

    void updatePasswordByEmail(String email, String encodedPassword);

    void updatePasswordById(Long userId, String encodedPassword);

    String getEncodePasswordByEmail(String email);

    String getEncodePasswordById(Long userId);

    String getRoleByUserId(Long userId);

    Long getUserIdByEmail(String email);

    void updateRoleToCommonUser(Long userId);

    void updateRoleToAdmin(Long userId);

    List<User> getAllUsers();

    /**
     * 分页查询所有用户。
     *
     * @param page     页码，从 1 开始
     * @param pageSize 每页大小
     * @return MyBatis-Plus IPage 分页对象
     */
    IPage<User> getAllUsers(int page, int pageSize);

    List<Map<String, Object>> getAllBookings(Long userId);

    /**
     * 分页查询用户的预约记录。
     */
    IPage<Map<String, Object>> getAllBookings(Long userId, int page, int pageSize);
}
