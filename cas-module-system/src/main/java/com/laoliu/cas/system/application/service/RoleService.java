package com.laoliu.cas.system.application.service;

public interface RoleService {

    String getRoleByUserId(Long userId);

    String changeRoleById(Long userId);
}
