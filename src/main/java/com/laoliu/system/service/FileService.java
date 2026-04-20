package com.laoliu.system.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author forever-king
 */
public interface FileService {

    String upload(MultipartFile file, Long userId);
}
