package com.laoliu.cas.appointment.infrastructure.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 预约记录数据对象 - 用于 MyBatis-Plus ORM
 *
 * @author forever-king
 */
@Data
@TableName("appointment_record")
public class AppointmentRecordDO {

    @TableId(type = IdType.AUTO)
    private Integer recordId;

    private Integer itemId;

    private Long userId;

    private String appointmentTime;

    private String appointmentPlace;

    private Integer appointmentState;

    private String description;

    private String createTime;

    public com.laoliu.cas.appointment.domain.entity.AppointmentRecord toEntity() {
        return new com.laoliu.cas.appointment.domain.entity.AppointmentRecord(
                recordId, itemId, userId, appointmentTime, appointmentPlace,
                appointmentState, description, createTime);
    }

    public static AppointmentRecordDO fromEntity(com.laoliu.cas.appointment.domain.entity.AppointmentRecord entity) {
        if (entity == null) return null;
        AppointmentRecordDO dataObject = new AppointmentRecordDO();
        dataObject.setRecordId(entity.getRecordId());
        dataObject.setItemId(entity.getItemId());
        dataObject.setUserId(entity.getUserId());
        dataObject.setAppointmentTime(entity.getAppointmentTime());
        dataObject.setAppointmentPlace(entity.getAppointmentPlace());
        dataObject.setAppointmentState(entity.getAppointmentState());
        dataObject.setDescription(entity.getDescription());
        dataObject.setCreateTime(entity.getCreateTime());
        return dataObject;
    }
}
