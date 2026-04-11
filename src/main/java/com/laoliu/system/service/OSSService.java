package com.laoliu.system.service;

import org.springframework.web.multipart.MultipartFile;


/**
 * @author 25516
 */
public interface OSSService {

    String uploadFile(MultipartFile file);

}
