package com.laoliu.system.controller;

import com.laoliu.system.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 25516
 */
@RestController
@RequestMapping("/hello")
@Tag(name = "Hello World")
public class Hello {
    private static final Logger log = LoggerFactory.getLogger(Hello.class);

    @GetMapping
    @Operation(summary = "Hello World")
    public CommonResult<String> hello() {
        // TODO：最好检查一下全项目的 REST API的编写规范，注意@PathVariable(指定使用)和@RequestParam(筛选使用)的使用
        log.info("hello world!!!!");
        return CommonResult.success("hello world");
    }
}
