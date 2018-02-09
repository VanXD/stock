/*
Navicat MySQL Data Transfer

Source Server         : DOCKER_99.100
Source Server Version : 50721
Source Host           : 192.168.99.100:3306
Source Database       : stock_analysis

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-02-09 15:08:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for today
-- ----------------------------
DROP TABLE IF EXISTS `today`;
CREATE TABLE `today` (
  `code` char(8) NOT NULL,
  `buy_price` double(7,3) NOT NULL,
  `today` date NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
