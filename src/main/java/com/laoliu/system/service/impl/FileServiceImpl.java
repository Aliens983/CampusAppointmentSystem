package com.laoliu.system.service.impl;

import com.laoliu.system.common.exception.enums.BusinessErrorCode;
import com.laoliu.system.exception.BusinessException;
import com.laoliu.system.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author 25516
 */
@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.prefix}")
    private String prefix;

    @Value("${file.upload.server-address}")
    private String serverAddress;

    private static final String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
    private static final String[] VIDEO_EXTENSIONS = {".mp4", ".avi", ".mov", ".wmv", ".flv", ".mkv"};

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.FILE_EMPTY);
        }

        String subDirectory = getSubDirectory(file);

        return saveFile(file, subDirectory);
    }

    private String getSubDirectory(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "files";
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

        for (String imgExt : IMAGE_EXTENSIONS) {
            if (extension.equals(imgExt)) {
                return "images";
            }
        }

        for (String videoExt : VIDEO_EXTENSIONS) {
            if (extension.equals(videoExt)) {
                return "videos";
            }
        }

        return "files";
    }

    private String saveFile(MultipartFile file, String subDirectory) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String newFileName = UUID.randomUUID().toString().replace("-", "") + extension;

        File directory = new File(uploadPath + subDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path filePath = Paths.get(uploadPath, subDirectory, newFileName);

        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new BusinessException(BusinessErrorCode.FILE_UPLOAD_FAILED);
        }

        return serverAddress + prefix + subDirectory + "/" + newFileName;
    }
}
