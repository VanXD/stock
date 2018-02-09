/*
Navicat MySQL Data Transfer

Source Server         : DOCKER_99.100
Source Server Version : 50721
Source Host           : 192.168.99.100:3306
Source Database       : stock_analysis

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-02-09 15:08:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for game
-- ----------------------------
DROP TABLE IF EXISTS `game`;
CREATE TABLE `game` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` char(8) NOT NULL,
  `mount` double NOT NULL,
  `buy_price` double(10,3) NOT NULL,
  `buy_date` date NOT NULL,
  `hold_day` int(11) NOT NULL DEFAULT '-1',
  `hold` int(11) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7582 DEFAULT CHARSET=utf8;
