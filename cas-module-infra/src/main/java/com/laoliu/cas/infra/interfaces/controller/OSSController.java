package com.laoliu.cas.infra.interfaces.controller;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.service.OSSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "对象存储")
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class OSSController {

    private final OSSService ossService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public CommonResult<String> upload(@RequestParam("file") MultipartFile file) {
        String url = ossService.uploadFile(file);
        return CommonResult.success(url);
    }
}
