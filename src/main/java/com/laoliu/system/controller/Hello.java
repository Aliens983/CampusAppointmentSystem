package com.laoliu.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 25516
 */
@RestController
@RequestMapping("/hello")
public class Hello {
    private static final Logger log = LoggerFactory.getLogger(Hello.class);

    @GetMapping
    public String hello(Model model) {
        model.addAttribute("name", "laoliu");
        model.addAttribute("hello");
        log.info("hello world!!!!");
        return "hello world";
    }
}
