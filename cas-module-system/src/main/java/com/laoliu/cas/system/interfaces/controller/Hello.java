package com.laoliu.cas.system.interfaces.controller;

import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author forever-king
 */
@Slf4j
@RestController
@RequestMapping("/hello")
@Tag(name = "Hello World")
public class Hello {

    @GetMapping
    @Operation(summary = "Hello World")
    public CommonResult<String> hello() {
        log.info("hello world!!!!");
        return CommonResult.success("hello world");
    }
}
