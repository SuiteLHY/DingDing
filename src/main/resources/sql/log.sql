/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql_server_user
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 11/01/2020 16:29:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '日志记录 - 编号',
  `userid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户 - 用户ID',
  `time` datetime(0) NOT NULL COMMENT '操作时间',
  `type` tinyint(3) UNSIGNED NOT NULL COMMENT '操作类型: {1:用户注册, 2:用户更新, 3:用户异常, 4:用户销毁, 5:用户数据删除, 11:用户登入, 12:用户登出}',
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记录的详细信息',
  `ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录的IP地址',
  `data_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_log_userid`(`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of log
-- ----------------------------
INSERT INTO `log` VALUES ('1', 'admin', '2019-12-30 11:35:41', 1, '测试数据', NULL, '2019-12-30 11:36:01');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户 - 用户ID',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户 - 密码',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户 - 昵称',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '用户 - 性别',
  `age` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户 - 年龄',
  `profilehead` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户 - 头像',
  `profile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户 - 简介',
  `firsttime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '注册时间',
  `lasttime` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登录时间',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登陆IP',
  `status` tinyint(1) NOT NULL COMMENT '账号状态 -> {0:已注销, 1:正常, 2:异常&禁用}',
  PRIMARY KEY (`userid`) USING BTREE,
  INDEX `userid`(`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('402880e56f88643d016f886449e10000', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:41:11', '2020-01-09 11:41:11', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88646b016f8864772a0000', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:41:23', '2020-01-09 11:41:23', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f886492016f88649e970000', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:41:33', '2020-01-09 11:41:33', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f8864df016f8864eb0d0000', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:41:52', '2020-01-09 11:41:52', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f886508016f8865143c0000', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:42:03', '2020-01-09 11:42:03', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f8865bb016f8865c7140000', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:42:49', '2020-01-09 11:42:49', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88661e016f886629f30000', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:43:14', '2020-01-09 11:43:14', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88661e016f88662a2e0001', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:43:14', '2020-01-09 11:43:14', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829710000', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829bf0001', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829c60002', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829cc0003', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829d20004', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829d90005', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829e10006', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829e60007', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829ee0008', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f88681d016f886829f40009', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 11:45:25', '2020-01-09 11:45:25', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('402880e56f89643e016f89645d8c0000', 'a12345678', '测试', 1, '20', NULL, '测试数据', '2020-01-09 16:20:50', '2020-01-09 16:20:50', '127.0.0.1', 1);
INSERT INTO `user` VALUES ('admin', 'admin', 'admin', 1, '23', 'upload/admin/admin.jpg', 'i\'m admin', '2017-01-11 19:22:21', '2017-01-11 19:23:20', NULL, 1);
INSERT INTO `user` VALUES ('zhoutao', '123456', 'zhoutao', 1, '23', 'upload/zhoutao/zhoutao.jpg', 'are you ok?', '2017-01-11 19:22:21', '2017-01-11 19:22:21', NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
