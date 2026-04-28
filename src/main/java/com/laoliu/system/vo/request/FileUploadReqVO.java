package com.laoliu.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 25516
 */
@Schema(description = "文件上传请求参数")
@Data
public class FileUploadReqVO {

    @Schema(description = "图片或者视频文件")
    private MultipartFile file;



}
