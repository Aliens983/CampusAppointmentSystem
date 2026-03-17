package com.laoliu.system.service.impl;

import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author 25516
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final UserMapper userMapper;

    public RoleServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public String changeRoleById(Long userId) {
        String role = userMapper.getRoleByUserId(userId);
        if (role != null) {
            if ("1".equals(role)){
                userMapper.updateRoleToCommonUser(userId);
                return "普通用户";
            }else if ("0".equals(role)){
                userMapper.updateRoleToAdmin(userId);
                return "管理员用户";
            }
        }
        return role;
    }

    @Override
    public String getRoleByUserId(Long userId) {
        String role = userMapper.getRoleByUserId(userId);
        if (role != null) {
            if ("1".equals(role)){
                return "管理员";
            }else if ("0".equals(role)){
                return "普通用户";
            }
        }
        return role;
    }
}
