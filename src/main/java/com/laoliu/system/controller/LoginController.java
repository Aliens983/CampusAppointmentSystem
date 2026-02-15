package com.laoliu.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 25516
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String verifyEmailCode() {
        return "登录成功";
    }
}
