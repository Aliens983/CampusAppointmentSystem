-- Campus Appointment System - Sample Data
-- 插入示例数据

USE campus_appointment;

-- 插入示例用户数据
-- role: 0-user (普通用户), 1-admin (管理员)
-- password: 明文密码，使用前请自行加密
INSERT INTO `user` (`name`, `grade`, `sex`, `age`, `email`, `password`, `role`) VALUES 
('张三', '大二', '男', 20, 'zhangsan@campus.com', '123456', 0),
('李四', '大三', '女', 21, 'lisi@campus.com', '123456', 0),
('王五', '大一', '男', 19, 'wangwu@campus.com', '123456', 0),
('赵六', '大四', '女', 22, 'zhaoliu@campus.com', '123456', 0),
('管理员', '系统管理员', '男', 30, 'admin@campus.com', 'admin123', 1);

-- 插入示例服务数据
-- service_state: 0-disabled, 1-enabled
INSERT INTO `service` (`service_name`, `service_describe`, `service_state`) VALUES 
('心理咨询', '提供专业的心理咨询服务，帮助学生缓解压力和焦虑', 1),
('学业辅导', '提供学科辅导和学习建议，帮助学生提高成绩', 1),
('就业指导', '提供简历修改和面试指导，帮助学生顺利就业', 1),
('健康检查', '提供常规健康检查和医疗咨询', 1),
('法律咨询', '提供法律知识咨询和法律问题解答', 1);

-- 插入示例订单数据
-- manage_status: 0-normal, 3-cancelled
INSERT INTO `item` (`user_id`, `service_id`, `manage_status`) VALUES 
(1, 1, 0),  -- 张三预约心理咨询
(1, 2, 0),  -- 张三预约学业辅导
(2, 1, 0),  -- 李四预约心理咨询
(3, 3, 0),  -- 王五预约就业指导
(4, 4, 0);  -- 赵六预约健康检查