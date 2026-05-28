package com.laoliu.system.service;

/**
 * @author 25516
 */
public interface RoleService {
    String getRoleByUserId(Long userId);

    // change the role and return the role of user.
    String changeRoleById(Long userId);
}
