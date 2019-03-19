/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50552
Source Host           : localhost:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50552
File Encoding         : 65001

Date: 2019-03-18 16:10:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for success_killed
-- ----------------------------
DROP TABLE IF EXISTS `success_killed`;
CREATE TABLE `success_killed` (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品ID',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

-- ----------------------------
-- Records of success_killed
-- ----------------------------
INSERT INTO `success_killed` VALUES ('1000', '12389075678', '1', '2019-03-18 14:47:05');
INSERT INTO `success_killed` VALUES ('1001', '12389075678', '1', '2019-03-18 08:55:01');
INSERT INTO `success_killed` VALUES ('1003', '12386541956', '1', '2019-03-18 08:47:39');
INSERT INTO `success_killed` VALUES ('1003', '12389075678', '1', '2019-03-18 08:49:51');
INSERT INTO `success_killed` VALUES ('1003', '13545968359', '1', '2019-03-17 21:25:01');
INSERT INTO `success_killed` VALUES ('1003', '15486531956', '1', '2019-03-17 21:15:49');
INSERT INTO `success_killed` VALUES ('1003', '15586531956', '1', '2019-03-17 21:13:48');
