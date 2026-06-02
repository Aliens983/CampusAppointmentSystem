package com.laoliu.cas.infra.interfaces.controller;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.thirdparty.service.OSSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author forever-king
 */
@Tag(name = "对象存储")
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class OSSController {

    private final OSSService ossService;

    @Operation(summary = "上传文件到OSS", description = "上传文件到阿里云OSS对象存储，返回文件的访问URL地址")
    @PostMapping("/upload")
    public CommonResult<String> upload(
            @Parameter(description = "待上传的文件", required = true) @RequestParam("file") MultipartFile file) {
        String url = ossService.uploadFile(file);
        return CommonResult.success(url);
    }
}
