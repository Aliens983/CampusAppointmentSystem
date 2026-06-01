package com.laoliu.cas.appointment.infrastructure.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * @author forever-king
 */
@Data
@TableName("item")
public class ItemDO {

    @TableId(type = IdType.AUTO)
    private Integer orderId;

    private Long userId;

    private Integer serviceId;

    private Date createTime;

    private Date updateTime;

    private Integer manageStatus;

    private String reason;
}
