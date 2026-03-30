package com.laoliu.system.controller;

import com.laoliu.system.mapper.UserMapper;
import com.laoliu.system.utils.JWTUtils;
import com.laoliu.system.utils.PasswordUtils;
import com.laoliu.system.vo.request.UserLoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author forever-king
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserMapper userMapper;

    private final JWTUtils jwtUtils;

    private final PasswordUtils passwordUtils;

    public LoginController(UserMapper userMapper, JWTUtils jwtUtils, PasswordUtils passwordUtils) {
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
        this.passwordUtils = passwordUtils;
    }


    @PostMapping
    public String login(@RequestBody UserLoginRequest userLoginRequest) {
        // TODO: 这里最好再查询一下数据库中是否有此邮箱，如果没有的话应该提醒用户邮箱输入错误或邮箱未注册

        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();
        if (email == null || password == null){
            return "邮箱和秘密都不能为空！！！";
        }

        String encodePassword = userMapper.getEncodePasswordByEmail(email);
        if (encodePassword == null){
            return "邮箱未注册！！！请先注册";
        }
        Long userId = userMapper.getUserIdByEmail(email);
        if (passwordUtils.matches(password, encodePassword)){
            return jwtUtils.generateToken(userId);
        }
        return "邮箱或密码错误！！！";
    }
}
