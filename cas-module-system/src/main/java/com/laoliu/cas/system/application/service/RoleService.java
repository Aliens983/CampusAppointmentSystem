package com.laoliu.cas.system.application.service;

/**
 * @author forever-king
 */
public interface RoleService {

    /** 根据用户ID获取角色 */
    String getRoleByUserId(Long userId);

    /** 切换用户角色 */
    String changeRoleById(Long userId);
}
