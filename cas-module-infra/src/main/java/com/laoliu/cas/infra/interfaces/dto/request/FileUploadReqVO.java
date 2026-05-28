package com.laoliu.cas.infra.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "文件上传请求")
public class FileUploadReqVO {

    @NotNull(message = "文件不能为空")
    @Schema(description = "文件")
    private MultipartFile file;
}
