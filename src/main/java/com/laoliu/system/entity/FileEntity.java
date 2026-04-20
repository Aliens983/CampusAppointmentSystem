package com.laoliu.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件信息实体类
 */
@Data
@TableName("file_info")
public class FileEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String fileName;

    private String filePath;

    private Long fileSize;

    private String fileType;

    private String fileExt;

    private String fileUuid;

    private Long uploadUser;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
