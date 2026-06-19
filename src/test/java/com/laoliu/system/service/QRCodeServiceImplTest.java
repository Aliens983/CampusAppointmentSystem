package com.laoliu.system.service;

import com.laoliu.system.exception.BusinessException;
import com.laoliu.system.service.impl.QRCodeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QRCodeServiceImplTest {

    @Mock
    private FileService fileService;

    private QRCodeServiceImpl qrCodeService;

    @BeforeEach
    void setUp() {
        qrCodeService = new QRCodeServiceImpl(fileService);
    }

    @Test
    @DisplayName("generateQrCode 空内容应抛出异常")
    void generateQrCode_emptyContent_shouldThrow() {
        assertThrows(BusinessException.class, () -> qrCodeService.generateQrCode(""));
    }

    @Test
    @DisplayName("generateQrCode null内容应抛出异常")
    void generateQrCode_nullContent_shouldThrow() {
        assertThrows(BusinessException.class, () -> qrCodeService.generateQrCode(null));
    }

    @Test
    @DisplayName("generateQrCode 空白内容应抛出异常")
    void generateQrCode_blankContent_shouldThrow() {
        assertThrows(BusinessException.class, () -> qrCodeService.generateQrCode("   "));
    }
}
