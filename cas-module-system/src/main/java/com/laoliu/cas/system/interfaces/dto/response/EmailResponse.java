package com.laoliu.cas.system.interfaces.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author forever-king
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "邮件响应类")
public class EmailResponse {

    @Schema(description = "响应消息")
    private String message;
}
