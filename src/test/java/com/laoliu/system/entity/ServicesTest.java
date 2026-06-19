package com.laoliu.system.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicesTest {

    @Test
    @DisplayName("setter和getter应正常工作")
    void settersAndGetters_shouldWorkCorrectly() {
        Services services = new Services();
        services.setServiceId(1);
        services.setServiceName("自习室预约");
        services.setServiceDescribe("提供自习室座位预约服务");
        services.setServiceState(1);

        assertEquals(1, services.getServiceId());
        assertEquals("自习室预约", services.getServiceName());
        assertEquals("提供自习室座位预约服务", services.getServiceDescribe());
        assertEquals(1, services.getServiceState());
    }

    @Test
    @DisplayName("equals null应返回false")
    void equals_null_shouldReturnFalse() {
        Services services = new Services();
        assertNotEquals(null, services);
    }

    @Test
    @DisplayName("equals 不同类应返回false")
    void equals_differentClass_shouldReturnFalse() {
        Services services = new Services();
        assertNotEquals("string", services);
    }

    @Test
    @DisplayName("toString 应包含服务信息")
    void toString_shouldContainServiceInfo() {
        Services services = new Services();
        services.setServiceId(1);
        services.setServiceName("测试服务");
        String str = services.toString();
        assertTrue(str.contains("serviceId=1"));
        assertTrue(str.contains("serviceName=测试服务"));
    }
}
