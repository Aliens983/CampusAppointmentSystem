package com.laoliu.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author 25516
 */
public interface FileService {
    String uploadFile(MultipartFile file);

    String uploadFile(File file);
}
