package com.laoliu.cas.system.infrastructure.persistence.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 预约记录查询结果 DO — 对应 item JOIN services 的查询结果。
 *
 * @author forever-king
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRecordDO {

    private String serviceName;
    private String serviceDescribe;
    private Date createTime;
    private Integer manageStatus;

}
