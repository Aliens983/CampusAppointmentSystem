package com.laoliu.system.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.laoliu.system.common.exception.enums.CommonErrorCode;
import com.laoliu.system.exception.BusinessException;
import com.laoliu.system.service.FileService;
import com.laoliu.system.service.QRCodeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author 25516
 */
@Service
@AllArgsConstructor
public class QRCodeServiceImpl implements QRCodeService {

    private final FileService fileService;

    @Override
    public String generateQrCode(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST);
        }
        File file = QrCodeUtil.generate(content, 300, 300, FileUtil.file("D:/qrcodes/qrcode.png"));
        return fileService.uploadFile(file);
    }
}
