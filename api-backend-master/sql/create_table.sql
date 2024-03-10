# 数据库初始化

-- 创建库
create database if not exists luckysj_api;

-- 切换库
use luckysj_api;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for call_record
-- ----------------------------
DROP TABLE IF EXISTS `call_record`;
CREATE TABLE `call_record`  (
                                `id` bigint NOT NULL COMMENT '主键id',
                                `user_id` bigint NOT NULL COMMENT '用户id',
                                `interface_id` bigint NOT NULL COMMENT '接口id',
                                `call_count` bigint NOT NULL DEFAULT 1 COMMENT '调用次数',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of call_record
-- ----------------------------
INSERT INTO `call_record` VALUES (1754412683328688129, 1753781344950779906, 3, 32, '2024-02-05 15:52:33', '2024-02-05 15:52:33');

-- ----------------------------
-- Table structure for interface_info
-- ----------------------------
DROP TABLE IF EXISTS `interface_info`;
CREATE TABLE `interface_info`  (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                   `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                   `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                   `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                   `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                   `request_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                   `response_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                   `call_cost` int NOT NULL,
                                   `call_count` bigint NOT NULL DEFAULT 0,
                                   `STATUS` int NOT NULL DEFAULT 0,
                                   `user_id` bigint NOT NULL,
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `is_delete` int NOT NULL DEFAULT 0,
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of interface_info
-- ----------------------------
INSERT INTO `interface_info` VALUES (1, '天气接口', '查询当地的天气信息', NULL, '/weather', 'Post', '{\"city_name\":\"431300\"}', '{\"code\":\"200\", \"msg\":\"ok\", \"data\":\"\"}', 1, 0, 1, 1753781344950779906, '2024-02-04 14:00:51', '2024-02-04 14:00:51', 1);
INSERT INTO `interface_info` VALUES (2, '测试', '测试', NULL, 'https://luckysj-1314434715.cos.ap-shanghai.myqcloud.com/4d06a73e-a97b-404e-98e2-4f7ee1b5952e.png', 'POST', '[{\"paramName\":\"id\",\"paramType\":\"String\",\"description\":\"哈哈哈\",\"isMust\":\"可选\"},{\"paramName\":\"name\",\"paramType\":\"String\",\"description\":\"名称\",\"isMust\":\"可选\"}]', NULL, 1, 0, 0, 1753781344950779906, '2024-02-05 15:18:00', '2024-02-05 15:18:00', 1);
INSERT INTO `interface_info` VALUES (3, '天气', '查询天气', 'https://luckysj-1314434715.cos.ap-shanghai.myqcloud.com/dd610493-b6e9-4dfa-b625-11992baa35f9.png', 'http://154.12.27.76:8102/api/weather', 'POST', '[{\"paramName\":\"city_name\",\"paramType\":\"String\",\"description\":\"查询地区天气(例如娄底市)\",\"isMust\":\"必须\"}]', '[{\"paramName\":\"code\",\"paramType\":\"int\",\"description\":\"状态码\"},{\"paramName\":\"data\",\"paramType\":\"Object\",\"description\":\"天气信息\"},{\"paramName\":\"msg\",\"paramType\":\"String\",\"description\":\"消息\"}]', 1, 32, 1, 1753781344950779906, '2024-02-05 15:47:27', '2024-02-05 15:47:27', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `account` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
                         `password` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                         `user_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
                         `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
                         `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
                         `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ak',
                         `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'sk',
                         `call_count` int NOT NULL DEFAULT 0 COMMENT '用户调用次数',
                         `role` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
                         `invite_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邀请码',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1759858209655459842 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1753781344950779906, 'kitie', '8025d79a60abb00f3ab7c38cb0757cf4', '用户itie', NULL, 'https://paopao-img-1320034963.cos.ap-beijing.myqcloud.com/images/userAvatar.png', '345b97965c3315ab0a0e09ebf5039e05', '2a1427f5417b0b5959a3e5f9fead4be3', 368, 'admin', '47va06', '2024-02-03 22:03:50', '2024-03-06 03:35:39', 0);
INSERT INTO `user` VALUES (1759453913411588098, 'zzkzzk', '938932227a53a22f033a5ce5a852f005', '用户kzzk', NULL, 'https://paopao-img-1320034963.cos.ap-beijing.myqcloud.com/images/userAvatar.png', '9f7900bb7e3a1909b9c83ed2b3c40676', 'c25d70b6284d7eb135b2e1f87c9ffe51', 0, 'admin', '1vsuek', '2024-02-19 13:44:36', '2024-02-19 13:45:18', 0);
INSERT INTO `user` VALUES (1759857483147481089, 'fking86', 'bed236f04d6449957be5096e95d4542a', '用户ng86', NULL, 'https://paopao-img-1320034963.cos.ap-beijing.myqcloud.com/images/userAvatar.png', 'a151a1680ce7e120337d5d521547c091', '7f00d26df36f8e63faaf540d635f896f', 0, 'user', 'wwzkv3', '2024-02-20 16:28:15', '2024-02-20 16:28:15', 0);
INSERT INTO `user` VALUES (1759858209655459841, 'wangbei', '979c18ec165780f04bff033b4e2b241d', '用户gbei', NULL, 'https://paopao-img-1320034963.cos.ap-beijing.myqcloud.com/images/userAvatar.png', 'e245ede7c10f4dc469a4402e0b8ac09a', '7099976abb544e2905cd4edaa25af69b', 0, 'user', 'qltgre', '2024-02-20 16:31:08', '2024-02-20 16:31:08', 0);

SET FOREIGN_KEY_CHECKS = 1;


