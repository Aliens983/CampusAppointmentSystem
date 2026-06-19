package com.laoliu.cas.system.interfaces.assembler;

import com.laoliu.cas.system.domain.entity.User;
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

    public List<UserResponse> convertToUserResponseList(List<User> users) {
        return users.stream().map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }
}
