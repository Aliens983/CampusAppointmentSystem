package com.laoliu.system.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    @DisplayName("无参构造应创建空对象")
    void noArgsConstructor_shouldCreateEmptyItem() {
        Item item = new Item();
        assertNull(item.getOrderId());
    }

    @Test
    @DisplayName("全参构造应设置所有字段")
    void allArgsConstructor_shouldSetAllFields() {
        Date now = new Date();
        Item item = new Item(1, 1L, 1, now, now, 0);

        assertEquals(1, item.getOrderId());
        assertEquals(1L, item.getUserId());
        assertEquals(1, item.getServiceId());
        assertEquals(now, item.getCreateTime());
        assertEquals(now, item.getUpdateTime());
        assertEquals(0, item.getManageStatus());
    }

    @Test
    @DisplayName("setter和getter应正常工作")
    void settersAndGetters_shouldWorkCorrectly() {
        Date now = new Date();
        Item item = new Item();
        item.setOrderId(1);
        item.setUserId(1L);
        item.setServiceId(2);
        item.setCreateTime(now);
        item.setUpdateTime(now);

        assertEquals(1, item.getOrderId());
        assertEquals(1L, item.getUserId());
        assertEquals(2, item.getServiceId());
        assertEquals(now, item.getCreateTime());
        assertEquals(now, item.getUpdateTime());
    }
}
