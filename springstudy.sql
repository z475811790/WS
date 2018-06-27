/*
Navicat MySQL Data Transfer

Source Server         : localDB
Source Server Version : 50628
Source Host           : localhost:3306
Source Database       : springstudy

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2018-06-28 00:41:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for marks
-- ----------------------------
DROP TABLE IF EXISTS `marks`;
CREATE TABLE `marks` (
  `SID` int(11) NOT NULL,
  `MARKS` int(11) NOT NULL,
  `YEAR` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of marks
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) NOT NULL,
  `AGE` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------

-- ----------------------------
-- Table structure for t_book
-- ----------------------------
DROP TABLE IF EXISTS `t_book`;
CREATE TABLE `t_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_book
-- ----------------------------
INSERT INTO `t_book` VALUES ('1', 'bookname0', '1234');
INSERT INTO `t_book` VALUES ('2', 'bookname0', '1234');
INSERT INTO `t_book` VALUES ('3', 'bookname0', '1234');
INSERT INTO `t_book` VALUES ('4', 'bookname0', '1234');
INSERT INTO `t_book` VALUES ('5', 'bookname0', '1234');
INSERT INTO `t_book` VALUES ('6', 'bookname0', '1234');
INSERT INTO `t_book` VALUES ('7', 'bookn', '1234');

-- ----------------------------
-- Table structure for t_courses
-- ----------------------------
DROP TABLE IF EXISTS `t_courses`;
CREATE TABLE `t_courses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `courses_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_courses
-- ----------------------------
INSERT INTO `t_courses` VALUES ('1', '语文');
INSERT INTO `t_courses` VALUES ('2', '数学');
INSERT INTO `t_courses` VALUES ('3', '计算机');
INSERT INTO `t_courses` VALUES ('4', 'java编程');
INSERT INTO `t_courses` VALUES ('5', 'html');

-- ----------------------------
-- Table structure for t_husband
-- ----------------------------
DROP TABLE IF EXISTS `t_husband`;
CREATE TABLE `t_husband` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_husband
-- ----------------------------
INSERT INTO `t_husband` VALUES ('2', 'hello');

-- ----------------------------
-- Table structure for t_key
-- ----------------------------
DROP TABLE IF EXISTS `t_key`;
CREATE TABLE `t_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `fk_lock_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_key
-- ----------------------------
INSERT INTO `t_key` VALUES ('1', '钥匙0', '1');
INSERT INTO `t_key` VALUES ('2', '钥匙1', '1');
INSERT INTO `t_key` VALUES ('3', '钥匙2', '1');
INSERT INTO `t_key` VALUES ('4', '钥匙3', '1');
INSERT INTO `t_key` VALUES ('5', '钥匙4', '1');

-- ----------------------------
-- Table structure for t_lock
-- ----------------------------
DROP TABLE IF EXISTS `t_lock`;
CREATE TABLE `t_lock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_lock
-- ----------------------------
INSERT INTO `t_lock` VALUES ('1', '锁1');

-- ----------------------------
-- Table structure for t_pet
-- ----------------------------
DROP TABLE IF EXISTS `t_pet`;
CREATE TABLE `t_pet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `bone` int(11) DEFAULT NULL,
  `fish` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_pet
-- ----------------------------
INSERT INTO `t_pet` VALUES ('1', '大脸猫', 'cat', null, '10');
INSERT INTO `t_pet` VALUES ('2', '哈士奇', 'dog', '10', null);

-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('1', '米兰');
INSERT INTO `t_student` VALUES ('2', '凌雪');
INSERT INTO `t_student` VALUES ('3', '成成');
INSERT INTO `t_student` VALUES ('4', '睿懿');
INSERT INTO `t_student` VALUES ('5', '瑞瑞');

-- ----------------------------
-- Table structure for t_student1
-- ----------------------------
DROP TABLE IF EXISTS `t_student1`;
CREATE TABLE `t_student1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(15) DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_student1
-- ----------------------------
INSERT INTO `t_student1` VALUES ('1', '小明', '男', '20');
INSERT INTO `t_student1` VALUES ('2', '小明', '男', '20');
INSERT INTO `t_student1` VALUES ('3', '小明', '男', '20');
INSERT INTO `t_student1` VALUES ('4', '小明', '男', '20');
INSERT INTO `t_student1` VALUES ('5', '小明', '男', '20');
INSERT INTO `t_student1` VALUES ('6', '小明', '男', '20');
INSERT INTO `t_student1` VALUES ('7', '小明3', '男', '20');
INSERT INTO `t_student1` VALUES ('8', '小明4', '男', '20');

-- ----------------------------
-- Table structure for t_stu_cou
-- ----------------------------
DROP TABLE IF EXISTS `t_stu_cou`;
CREATE TABLE `t_stu_cou` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_stu_id` int(11) DEFAULT NULL,
  `fk_cou_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_stu_cou
-- ----------------------------
INSERT INTO `t_stu_cou` VALUES ('1', '1', '1');
INSERT INTO `t_stu_cou` VALUES ('2', '1', '2');
INSERT INTO `t_stu_cou` VALUES ('3', '2', '3');
INSERT INTO `t_stu_cou` VALUES ('4', '2', '4');
INSERT INTO `t_stu_cou` VALUES ('5', '3', '1');
INSERT INTO `t_stu_cou` VALUES ('6', '3', '5');
INSERT INTO `t_stu_cou` VALUES ('7', '4', '4');
INSERT INTO `t_stu_cou` VALUES ('8', '4', '2');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `account` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('2', 'kitty', '1314520', '7000.0');
INSERT INTO `t_user` VALUES ('3', 'kitty', '1314520', '7000.0');
INSERT INTO `t_user` VALUES ('4', 'kitty', '1314520', '7000.0');
INSERT INTO `t_user` VALUES ('5', 'kitty', '1314520', '7000.0');
INSERT INTO `t_user` VALUES ('6', 'kitty', '1314520', '7000.0');
INSERT INTO `t_user` VALUES ('7', 'kitty', '1314520', '7000.0');
INSERT INTO `t_user` VALUES ('8', 'kitty0', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('9', 'kitty1', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('10', 'kitty2', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('11', 'kitty3', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('12', 'kitty4', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('13', 'kitty5', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('14', 'kitty6', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('15', 'kitty7', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('16', 'kitty8', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('17', 'kitty9', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('18', 'kitty0', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('19', 'kitty1', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('20', 'kitty2', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('21', 'kitty3', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('22', 'kitty4', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('23', 'kitty5', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('24', 'kitty6', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('25', 'kitty7', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('26', 'kitty8', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('27', 'kitty9', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('28', 'kitty0', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('29', 'kitty1', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('30', 'kitty2', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('31', 'kitty3', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('32', 'kitty4', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('33', 'kitty5', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('34', 'kitty6', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('35', 'kitty7', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('36', 'kitty8', '123456', '6000.0');
INSERT INTO `t_user` VALUES ('37', 'kitty9', '123456', '6000.0');

-- ----------------------------
-- Table structure for t_wife
-- ----------------------------
DROP TABLE IF EXISTS `t_wife`;
CREATE TABLE `t_wife` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `fk_husband_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_wife
-- ----------------------------
INSERT INTO `t_wife` VALUES ('1', 'kitty', '2');

-- ----------------------------
-- Procedure structure for getRecord
-- ----------------------------
DROP PROCEDURE IF EXISTS `getRecord`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRecord`(
IN in_id INTEGER,
OUT out_name VARCHAR(20),
OUT out_age  INTEGER)
BEGIN
   SELECT name, age
   INTO out_name, out_age
   FROM Student where id = in_id;
END
;;
DELIMITER ;
SET FOREIGN_KEY_CHECKS=1;
