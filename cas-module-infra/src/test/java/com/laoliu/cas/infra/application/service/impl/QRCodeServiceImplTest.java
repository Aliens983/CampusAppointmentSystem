package com.laoliu.cas.infra.application.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.laoliu.cas.common.exception.BusinessException;
import com.laoliu.cas.common.exception.code.CommonErrorCode;
import com.laoliu.cas.infra.application.service.QRCodeService;
import com.laoliu.cas.thirdparty.application.service.OSSService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * QRCodeServiceImpl 单元测试。
 *
 * @author forever-king
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("二维码服务单元测试")
class QRCodeServiceImplTest {

    @Mock
    private OSSService ossService;

    private MockedStatic<QrCodeUtil> qrCodeUtilMock;

    private QRCodeService qrCodeService;

    private static final String CONTENT = "https://example.com/booking/123";
    private static final String UPLOAD_URL = "https://oss.example.com/qrcodes/abc123.png";

    @BeforeEach
    void setUp() {
        qrCodeUtilMock = mockStatic(QrCodeUtil.class);
        qrCodeService = new QRCodeServiceImpl(ossService);
    }

    @AfterEach
    void tearDown() {
        qrCodeUtilMock.close();
    }

    @Nested
    @DisplayName("生成二维码 - generateQrCode")
    class GenerateQrCodeTests {

        @Test
        @DisplayName("应当成功生成二维码并返回 OSS 上传地址")
        void shouldGenerateQrCodeSuccessfully() {
            // Given
            BufferedImage mockImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
            qrCodeUtilMock.when(() -> QrCodeUtil.generate(CONTENT, 300, 300)).thenReturn(mockImage);
            when(ossService.uploadFile(any(MultipartFile.class))).thenReturn(UPLOAD_URL);

            // When
            String result = qrCodeService.generateQrCode(CONTENT);

            // Then
            assertNotNull(result);
            assertEquals(UPLOAD_URL, result);
            verify(ossService).uploadFile(any(MultipartFile.class));
        }

        @Test
        @DisplayName("内容为 null 时应当抛出 BAD_REQUEST 异常")
        void shouldThrowExceptionWhenContentIsNull() {
            // When & Then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> qrCodeService.generateQrCode(null));
            assertEquals(CommonErrorCode.BAD_REQUEST.getCode(), exception.getCode());
            verify(ossService, never()).uploadFile(any(MultipartFile.class));
        }

        @Test
        @DisplayName("内容为空字符串时应当抛出 BAD_REQUEST 异常")
        void shouldThrowExceptionWhenContentIsBlank() {
            // When & Then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> qrCodeService.generateQrCode("   "));
            assertEquals(CommonErrorCode.BAD_REQUEST.getCode(), exception.getCode());
            verify(ossService, never()).uploadFile(any(MultipartFile.class));
        }
    }
}
