package com.laoliu.cas.system.interfaces.assembler;

import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.system.infrastructure.persistence.dataobject.UserDO;
import com.laoliu.cas.system.interfaces.dto.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author forever-king
 */
@Component
public class UserAssembler {

    public UserResponse convertToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setGrade(user.getGrade());
        response.setSex(user.getSex());
        response.setAge(user.getAge());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }

    public List<UserResponse> convertToUserResponseList(List<UserDO> users) {
        return users.stream().map(userDO -> {
            UserResponse response = new UserResponse();
            response.setId(userDO.getId());
            response.setName(userDO.getName());
            response.setGrade(userDO.getGrade());
            response.setSex(userDO.getSex());
            response.setAge(userDO.getAge());
            response.setEmail(userDO.getEmail());
            response.setRole(userDO.getRole());
            return response;
        }).collect(Collectors.toList());
    }
}
