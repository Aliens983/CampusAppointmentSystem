package com.laoliu.system.controller;

import com.laoliu.system.service.OSSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author 25516
 */
@Tag(name = "对象存储")
@RestController
@RequestMapping("/oss")
public class OSSController {

    private final OSSService ossService;

    public OSSController(OSSService ossService) {
        this.ossService = ossService;
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public String upload(@RequestParam("file") MultipartFile file) {
        return ossService.uploadFile(file);
    }
}
