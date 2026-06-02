package com.laoliu.cas.infra.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileService {
    String uploadFile(MultipartFile file);

    String uploadFile(File file);
}
