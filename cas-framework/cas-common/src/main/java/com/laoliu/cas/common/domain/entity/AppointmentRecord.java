package com.laoliu.cas.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("appointment_record")
public class AppointmentRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer recordId;

    private Integer itemId;

    private Integer userId;

    private String appointmentTime;

    private String appointmentPlace;

    private Integer appointmentState;

    private String description;

    private String createTime;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentPlace() {
        return appointmentPlace;
    }

    public void setAppointmentPlace(String appointmentPlace) {
        this.appointmentPlace = appointmentPlace;
    }

    public Integer getAppointmentState() {
        return appointmentState;
    }

    public void setAppointmentState(Integer appointmentState) {
        this.appointmentState = appointmentState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AppointmentRecord other = (AppointmentRecord) that;
        return (this.getRecordId() != null ? this.getRecordId().equals(other.getRecordId()) : other.getRecordId() == null);
    }

    @Override
    public int hashCode() {
        return (recordId != null ? recordId.hashCode() : 0);
    }
}
