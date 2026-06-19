package com.laoliu.system.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("无参构造应创建空对象")
    void noArgsConstructor_shouldCreateEmptyUser() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
    }

    @Test
    @DisplayName("setter和getter应正常工作")
    void settersAndGetters_shouldWorkCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setGrade("2024级");
        user.setSex("男");
        user.setAge(20);
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole(0);

        assertEquals(1L, user.getId());
        assertEquals("张三", user.getName());
        assertEquals("2024级", user.getGrade());
        assertEquals("男", user.getSex());
        assertEquals(20, user.getAge());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(0, user.getRole());
    }

    @Test
    @DisplayName("equals 相同对象应返回true")
    void equals_sameObject_shouldReturnTrue() {
        User user = new User();
        assertEquals(user, user);
    }

    @Test
    @DisplayName("equals null应返回false")
    void equals_null_shouldReturnFalse() {
        User user = new User();
        assertNotEquals(null, user);
    }

    @Test
    @DisplayName("equals 同类不同值应返回false")
    void equals_differentValues_shouldReturnFalse() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("张三");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("李四");

        assertNotEquals(user1, user2);
    }

    @Test
    @DisplayName("hashCode 相同id的user应相同")
    void hashCode_sameId_shouldBeEqual() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("张三");

        User user2 = new User();
        user2.setId(1L);
        user2.setName("张三");

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("toString 应包含所有字段")
    void toString_shouldContainAllFields() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        String str = user.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("name=张三"));
    }
}
