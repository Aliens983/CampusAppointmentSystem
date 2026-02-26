package com.laoliu.system.converter;

import com.laoliu.system.entity.User;
import com.laoliu.system.vo.request.UserRequest;
import com.laoliu.system.vo.response.UserResponse;
import org.springframework.stereotype.Component;

/**
 * @author 25516
 */
@Component
public class UserConverter {



    public User convertUserRequestToUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setGrade(userRequest.getGrade());
        user.setSex(userRequest.getSex());
        user.setAge(userRequest.getAge());
        user.setRole(userRequest.getRole());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        return user;
    }

    public UserResponse convertUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        if (user.getRole()==1){
            userResponse.setRole("Admin");
        }
        if (user.getRole()==0){
            userResponse.setRole("User");
        }


        return userResponse;
    }

}
