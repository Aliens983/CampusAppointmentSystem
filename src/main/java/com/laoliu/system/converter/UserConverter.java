package com.laoliu.system.converter;

import com.laoliu.system.entity.User;
import com.laoliu.system.vo.request.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User convertUserRequestToUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setGrade(userRequest.getGrade());
        user.setSex(userRequest.getSex());
        user.setAge(userRequest.getAge());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        return user;
    }

}
