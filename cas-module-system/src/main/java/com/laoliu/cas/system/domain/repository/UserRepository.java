package com.laoliu.cas.system.domain.repository;

import com.laoliu.cas.system.domain.entity.User;

import java.util.List;
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

    List<java.util.Map<String, Object>> getAllBookings(Long userId);
}
