package com.laoliu.cas.infra.interfaces.controller;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.infra.application.service.FileService;
import com.laoliu.cas.infra.interfaces.dto.request.FileUploadReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author forever-king
 */
@Tag(name = "文件上传接口")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件,并且获取上传文件的URL")
    public CommonResult<String> uploadFile(@Validated FileUploadReqVO fileUploadReqVO) {
        String fileUrl = fileService.uploadFile(fileUploadReqVO.getFile());
        return CommonResult.success(fileUrl);
    }
}
