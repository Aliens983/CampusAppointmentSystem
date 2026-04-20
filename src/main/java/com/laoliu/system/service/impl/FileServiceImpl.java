package com.laoliu.system.service.impl;

import com.laoliu.system.entity.FileEntity;
import com.laoliu.system.mapper.FileMapper;
import com.laoliu.system.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * @author forever-king
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    public FileServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public String upload(MultipartFile file, Long userId) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileUuid = UUID.randomUUID().toString().replace("-", "");
        String newFileName = fileUuid + fileExt;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File destFile = new File(uploadPath, newFileName);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            log.error("文件保存失败", e);
            throw new RuntimeException("文件保存失败: " + e.getMessage());
        }

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(originalFilename);
        fileEntity.setFilePath(uploadPath + "/" + newFileName);
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setFileExt(fileExt);
        fileEntity.setFileUuid(fileUuid);
        fileEntity.setUploadUser(userId);
        fileEntity.setCreateTime(LocalDateTime.now());
        fileEntity.setUpdateTime(LocalDateTime.now());
        fileEntity.setIsDeleted(0);

        fileMapper.insert(fileEntity);

        return "/files/" + newFileName;
    }
}
