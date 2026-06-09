package com.laoliu.cas.infra.application.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.laoliu.cas.common.exception.BusinessException;
import com.laoliu.cas.common.exception.code.CommonErrorCode;
import com.laoliu.cas.infra.application.service.QRCodeService;
import com.laoliu.cas.thirdparty.application.service.OSSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author forever-king
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QRCodeServiceImpl implements QRCodeService {

    private final OSSService ossService;

    @Override
    public String generateQrCode(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST);
        }

        try {
            BufferedImage qrCodeImage = QrCodeUtil.generate(content, 300, 300);
            byte[] qrCodeBytes = bufferedImageToBytes(qrCodeImage);
            MultipartFile multipartFile = new ByteArrayMultipartFile(qrCodeBytes, UUID.randomUUID().toString() + ".png");
            return ossService.uploadFile(multipartFile);
        } catch (Exception e) {
            log.error("生成二维码失败", e);
            throw new RuntimeException("生成二维码失败：" + e.getMessage(), e);
        }
    }

    private byte[] bufferedImageToBytes(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    private static class ByteArrayMultipartFile implements MultipartFile {
        private final byte[] content;
        private final String filename;

        public ByteArrayMultipartFile(byte[] content, String filename) {
            this.content = content;
            this.filename = filename;
        }

        @Override
        public String getName() {
            return filename;
        }

        @Override
        public String getOriginalFilename() {
            return filename;
        }

        @Override
        public String getContentType() {
            return "image/png";
        }

        @Override
        public boolean isEmpty() {
            return content == null || content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public java.io.InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException {
            java.nio.file.Files.write(dest.toPath(), content);
        }
    }
}
