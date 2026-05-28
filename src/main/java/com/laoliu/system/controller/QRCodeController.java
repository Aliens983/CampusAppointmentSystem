package com.laoliu.system.controller;

import com.laoliu.system.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.laoliu.system.service.QRCodeService;

/**
 * @author 25516
 */
@Tag(name = "二维码生成接口")
@RestController
@RequestMapping("/qr_code")
@AllArgsConstructor
public class QRCodeController {

    private final QRCodeService qRCodeService;

    @Operation(summary = "生成二维码")
    @GetMapping("/generate")
    public CommonResult<String> generateQrCode(@RequestParam @Parameter(description = "二维码内容") String content) {
        return CommonResult.success(qRCodeService.generateQrCode(content));
    }


}
