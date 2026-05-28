package com.laoliu.cas.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface OSSService {

    String uploadFile(MultipartFile file);
}
