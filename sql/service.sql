-- Campus Appointment System - Service Table
-- 创建服务表

USE campus_appointment;

DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `service_id` INT NOT NULL AUTO_INCREMENT COMMENT 'Service ID',
  `service_name` VARCHAR(100) NOT NULL COMMENT 'Service name',
  `service_describe` TEXT DEFAULT NULL COMMENT 'Service description',
  `service_state` TINYINT NOT NULL DEFAULT 1 COMMENT 'Service status: 0-disabled, 1-enabled',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update time',
  PRIMARY KEY (`service_id`),
  KEY `idx_service_name` (`service_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Service table - stores appointment service information';