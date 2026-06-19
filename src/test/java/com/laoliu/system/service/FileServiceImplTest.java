package com.laoliu.system.service;

import com.laoliu.system.exception.BusinessException;
import com.laoliu.system.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceImplTest {

    private FileServiceImpl fileService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileService = new FileServiceImpl();
        ReflectionTestUtils.setField(fileService, "uploadPath", tempDir.toString() + "/");
        ReflectionTestUtils.setField(fileService, "prefix", "/api/files/");
        ReflectionTestUtils.setField(fileService, "serverAddress", "http://localhost:8080");
    }

    @Test
    @DisplayName("上传空文件应抛出异常")
    void uploadFile_emptyFile_shouldThrow() {
        MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);
        assertThrows(BusinessException.class, () -> fileService.uploadFile(emptyFile));
    }

    @Test
    @DisplayName("上传null文件应抛出异常")
    void uploadFile_nullFile_shouldThrow() {
        assertThrows(BusinessException.class, () -> fileService.uploadFile((MockMultipartFile) null));
    }

    @Test
    @DisplayName("上传图片文件应返回正确URL")
    void uploadFile_image_shouldReturnCorrectUrl() {
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "test image content".getBytes());

        String url = fileService.uploadFile(imageFile);

        assertTrue(url.startsWith("http://localhost:8080/api/files/images/"));
        assertTrue(url.endsWith(".jpg"));
    }

    @Test
    @DisplayName("上传视频文件应返回正确URL")
    void uploadFile_video_shouldReturnCorrectUrl() {
        MockMultipartFile videoFile = new MockMultipartFile(
                "file", "test.mp4", "video/mp4", "test video content".getBytes());

        String url = fileService.uploadFile(videoFile);

        assertTrue(url.startsWith("http://localhost:8080/api/files/videos/"));
        assertTrue(url.endsWith(".mp4"));
    }

    @Test
    @DisplayName("上传其他文件应放入files目录")
    void uploadFile_otherFile_shouldGoToFilesDir() {
        MockMultipartFile otherFile = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "test content".getBytes());

        String url = fileService.uploadFile(otherFile);

        assertTrue(url.startsWith("http://localhost:8080/api/files/files/"));
        assertTrue(url.endsWith(".pdf"));
    }

    @Test
    @DisplayName("上传无扩展名文件应返回files目录")
    void uploadFile_noExtension_shouldReturnFilesDir() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "testfile", "application/octet-stream", "test content".getBytes());

        String url = fileService.uploadFile(file);

        assertTrue(url.startsWith("http://localhost:8080/api/files/files/"));
    }

    @Test
    @DisplayName("上传File对象应成功")
    void uploadFile_fileObject_shouldSucceed() throws IOException {
        File testFile = tempDir.resolve("test.txt").toFile();
        testFile.createNewFile();

        String url = fileService.uploadFile(testFile);

        assertTrue(url.startsWith("http://localhost:8080/api/files/files/"));
        assertTrue(url.endsWith(".txt"));
    }

    @Test
    @DisplayName("上传不存在的File对象应抛出异常")
    void uploadFile_nonexistentFile_shouldThrow() {
        File nonexistent = new File("/nonexistent/file.txt");
        assertThrows(BusinessException.class, () -> fileService.uploadFile(nonexistent));
    }
}
