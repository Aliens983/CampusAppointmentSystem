package com.laoliu.system.converter;

import com.laoliu.system.entity.User;
import com.laoliu.system.vo.request.UserRegisterRequest;
import com.laoliu.system.vo.response.UserResponse;
import org.springframework.stereotype.Component;

/**
 * @author 25516
 */
@Component
public class UserConverter {



    public User convertUserRequestToUser(UserRegisterRequest userRegisterRequest) {
        User user = new User();
        user.setName(userRegisterRequest.getName());
        user.setGrade(userRegisterRequest.getGrade());
        user.setSex(userRegisterRequest.getSex());
        user.setAge(userRegisterRequest.getAge());
        user.setRole(userRegisterRequest.getRole());
        user.setEmail(userRegisterRequest.getEmail());
        user.setPassword(userRegisterRequest.getPassword());
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
