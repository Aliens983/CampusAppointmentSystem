package com.laoliu.system.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.laoliu.system.config.OSSConfig;
import com.laoliu.system.service.OSSService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云OSS服务类
 * @author 25516
 */
@Service
public class OSSServiceImpl implements OSSService {

    private final OSSConfig ossConfig;

    public OSSServiceImpl(OSSConfig ossConfig) {
        this.ossConfig = ossConfig;
    }

    /**
     * 上传文件到阿里云OSS
     * @param file 上传的文件
     * @return 文件的URL
     */
    @Override
    public String uploadFile(MultipartFile file) {
        // 创建OSS客户端
        OSS ossClient = new OSSClientBuilder().build(
                ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
        );

        try {
            // 生成唯一的文件名
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "-" + originalFilename;

            // 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 创建PutObjectRequest对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    fileName,
                    inputStream
            );

            // 上传文件
            ossClient.putObject(putObjectRequest);

            // 生成文件的URL
            return "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }
    }
}