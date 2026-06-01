package com.laoliu.cas.system.application.service;

/**
 * @author forever-king
 */
public interface RoleService {

    String getRoleByUserId(Long userId);

    String changeRoleById(Long userId);
}
