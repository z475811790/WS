/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50173
Source Host           : localhost:3306
Source Database       : xyzserver

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-09-28 10:26:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id编号',
  `user_name` varchar(15) DEFAULT NULL COMMENT '名字',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `birthday` timestamp NULL DEFAULT NULL COMMENT '生日',
  `pro` varchar(100) DEFAULT NULL COMMENT '属性 1,2;3,4;5,6',
  `reward_info` varchar(255) DEFAULT NULL COMMENT '奖励信息 1,2,3,4;5,6,7,8',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('1', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('2', '小明5', '男', '16', '2018-07-01 15:22:41', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('3', '小明5', '男', '21', '2018-07-01 15:22:43', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('4', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('5', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('6', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('7', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('8', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('9', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('10', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('11', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('12', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('13', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('14', '小明5', '男', '16', '2018-07-01 15:22:41', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('15', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('16', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('17', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('18', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('19', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('20', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('21', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('22', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('23', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('24', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('26', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('28', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('29', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('31', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('32', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');
INSERT INTO `t_student` VALUES ('33', '小明5', '男', '28', '2018-07-01 15:22:37', '1,2;3,4', '1,2,3,4;2,3,4,5');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(16) DEFAULT '' COMMENT '账号',
  `password` varchar(16) DEFAULT '' COMMENT '密码',
  `is_admin` bit(1) DEFAULT NULL COMMENT '是否为管理员',
  `create_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'xyzdl', '132456', '', '2018-09-27 14:58:20');
