package com.laoliu.cas.infra.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author forever-king
 */
public interface FileService {
    String uploadFile(MultipartFile file);

    String uploadFile(File file);
}
