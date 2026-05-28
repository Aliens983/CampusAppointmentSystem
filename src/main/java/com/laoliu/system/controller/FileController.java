package com.laoliu.system.controller;

import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.service.FileService;
import com.laoliu.system.vo.request.FileUploadReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 25516
 */
@Tag(name = "文件上传接口")
@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件,并且获取上传文件的URL")
    public CommonResult<String> uploadFile(@Validated FileUploadReqVO fileUploadReqVO) {
        String fileUrl = fileService.uploadFile(fileUploadReqVO.getFile());
        return CommonResult.success(fileUrl);
    }
}
