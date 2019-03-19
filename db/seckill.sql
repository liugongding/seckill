/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50552
Source Host           : localhost:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50552
File Encoding         : 65001

Date: 2019-03-19 09:44:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for seckill
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `start_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀结束时间',
  `version` varchar(10) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- ----------------------------
-- Records of seckill
-- ----------------------------
INSERT INTO `seckill` VALUES ('1000', '1000元秒杀iphone6', '86', '2019-03-11 21:10:33', '2019-03-16 00:00:00', '2019-03-30 00:00:00', '3');
INSERT INTO `seckill` VALUES ('1001', '800元秒杀ipad', '198', '2019-03-11 21:10:33', '2019-03-19 08:55:00', '2019-03-30 00:00:00', '1');
INSERT INTO `seckill` VALUES ('1002', '6600元秒杀mac book pro', '300', '2019-03-11 21:10:33', '2019-03-14 00:00:00', '2019-03-16 00:00:00', '0');
