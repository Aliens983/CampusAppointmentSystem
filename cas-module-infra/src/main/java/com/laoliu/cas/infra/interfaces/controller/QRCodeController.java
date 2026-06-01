package com.laoliu.cas.infra.interfaces.controller;

import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.service.QRCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author forever-king
 */
@Tag(name = "二维码生成接口")
@RestController
@RequestMapping("/qr_code")
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeService qRCodeService;

    @Operation(summary = "生成二维码", description = "根据提供的文本内容生成对应的二维码图片，返回Base64编码的图片数据")
    @GetMapping("/generate")
    public CommonResult<String> generateQrCode(@RequestParam @Parameter(description = "二维码内容", required = true) String content) {
        return CommonResult.success(qRCodeService.generateQrCode(content));
    }
}
