/*
Navicat MySQL Data Transfer

Source Server         : DOCKER_99.100
Source Server Version : 50721
Source Host           : 192.168.99.100:3306
Source Database       : stock_analysis

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-02-09 15:14:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for maxplus_match
-- ----------------------------
DROP TABLE IF EXISTS `maxplus_match`;
CREATE TABLE `maxplus_match` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `progress_desc` varchar(10) NOT NULL,
  `title` varchar(200) NOT NULL,
  `sub_title` varchar(200) NOT NULL,
  `end_bid_time` datetime NOT NULL,
  `win_team_name` varchar(50) NOT NULL,
  `lose_team_name` varchar(50) NOT NULL,
  `win_team_score` int(11) NOT NULL,
  `lose_team_score` int(11) NOT NULL,
  `match_id` int(11) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_match_id` (`match_id`),
  KEY `idx_win_team_name` (`win_team_name`),
  KEY `idx_lose_team_name` (`lose_team_name`),
  KEY `idx_win_lose_team_name` (`win_team_name`,`lose_team_name`)
) ENGINE=InnoDB AUTO_INCREMENT=275033 DEFAULT CHARSET=utf8;