package com.laoliu.cas.common.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author forever-king
 */
public interface OSSService {

    String uploadFile(MultipartFile file);
}
