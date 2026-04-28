package com.laoliu.system.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 25516
 */
public interface FileService {
    String uploadFile(MultipartFile file);
}
