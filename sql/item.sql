-- Campus Appointment System - Item Table (Order Table)
-- 创建订单表

USE campus_appointment;

DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `order_id` INT NOT NULL AUTO_INCREMENT COMMENT 'Order ID',
  `user_id` BIGINT NOT NULL COMMENT 'User ID, foreign key to user.id',
  `service_id` INT NOT NULL COMMENT 'Service ID, foreign key to service.service_id',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Order creation time',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Order update time',
  PRIMARY KEY (`order_id`),
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_service_id` (`service_id`) USING BTREE,
  CONSTRAINT `fk_item_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_item_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Order table - stores user appointment orders';