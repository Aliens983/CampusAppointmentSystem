-- Campus Appointment System - User Table
-- 创建用户表

USE cas_db;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key, auto-increment',
  `name` VARCHAR(50) NOT NULL COMMENT 'User name, cannot be null',
  `grade` VARCHAR(20) DEFAULT NULL COMMENT 'Academic grade or class, optional',
  `sex` VARCHAR(10) DEFAULT NULL COMMENT 'Gender: male/female/other',
  `age` TINYINT UNSIGNED DEFAULT NULL COMMENT 'Age, unsigned (0-127), optional',
  `email` VARCHAR(100) DEFAULT NULL COMMENT 'Email, must be unique if provided',
  `password` VARCHAR(255) NOT NULL COMMENT 'Hashed password (e.g., BCrypt), never store plain text!',
  `role` TINYINT NOT NULL DEFAULT 0 COMMENT 'Your role in this system. 1 is admin, 0 is user.',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`) USING BTREE,
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User table - stores student and admin information';