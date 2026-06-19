package com.laoliu.cas.appointment.domain.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 预约记录领域实体
 * 纯净，不依赖任何框架注解
 *
 * @author forever-king
 */
public class AppointmentRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer recordId;

    private Integer itemId;

    private Long userId;

    private String appointmentTime;

    private String appointmentPlace;

    private Integer appointmentState;

    private String description;

    private String createTime;

    public AppointmentRecord() {
    }

    public AppointmentRecord(Integer recordId, Integer itemId, Long userId, String appointmentTime,
                             String appointmentPlace, Integer appointmentState, String description, String createTime) {
        this.recordId = recordId;
        this.itemId = itemId;
        this.userId = userId;
        this.appointmentTime = appointmentTime;
        this.appointmentPlace = appointmentPlace;
        this.appointmentState = appointmentState;
        this.description = description;
        this.createTime = createTime;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentRecord that = (AppointmentRecord) o;
        return Objects.equals(recordId, that.recordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }

    @Override
    public String toString() {
        return "AppointmentRecord{" +
                "recordId=" + recordId +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", appointmentState=" + appointmentState +
                '}';
    }
}
