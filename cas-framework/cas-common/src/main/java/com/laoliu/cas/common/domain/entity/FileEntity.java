package com.laoliu.cas.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author forever-king
 */
@Data
@TableName("file_entity")
@EqualsAndHashCode
public class FileEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String fileName;

    private String fileUrl;

    private Integer fileSize;

    private Integer uploadUserId;

    private String uploadTime;

}
