/*
Navicat MySQL Data Transfer

Source Server         : 135
Source Server Version : 50718
Source Host           : 192.168.1.135:3306
Source Database       : cms_dev

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-11-15 10:20:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cms_content
-- ----------------------------
DROP TABLE IF EXISTS `cms_content`;
CREATE TABLE `cms_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL COMMENT '标题',
  `content` longtext COMMENT '内容',
  `url` json DEFAULT NULL COMMENT '附件保存路径',
  `type` tinyint(8) DEFAULT NULL COMMENT '内容分类[0=通知，1=消息，2=文章，3=投诉与不良事件，4=技术咨询]',
  `create_by` bigint(11) DEFAULT NULL COMMENT '文章创建人',
  `release_by` bigint(11) DEFAULT NULL COMMENT '发布人Id',
  `from_tenant_id` bigint(20) DEFAULT NULL COMMENT '来源机构id',
  `from_tenant_name` varchar(255) DEFAULT NULL COMMENT '来源机构名称',
  `from_dept_name` varchar(255) DEFAULT NULL COMMENT '来源部门名称',
  `to_tenant_ids` text COMMENT '指定的机构的ids',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '文章创建时间',
  `release_person_name` varchar(255) DEFAULT NULL COMMENT '发布人姓名',
  `release_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_by` bigint(11) DEFAULT NULL COMMENT '文章更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '文章更新时间',
  `expire_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '文章到期时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `enable` bit(1) DEFAULT b'1' COMMENT '启用标记 1 启用 0 停用',
  `del_flag` bit(1) DEFAULT b'0' COMMENT '删除标记 1 已删除,0 未删除',
  `custom_data` json DEFAULT NULL COMMENT '扩展字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8 COMMENT='内容表';

-- ----------------------------
-- Table structure for cms_content_user
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_user`;
CREATE TABLE `cms_content_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` bigint(20) NOT NULL COMMENT '内容id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `del_flag` bit(1) DEFAULT b'0' COMMENT '是否删除[0=未删除，1=删除]',
  `creat_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `custom_data` json DEFAULT NULL COMMENT '扩展字段',
  PRIMARY KEY (`id`),
  KEY `content_id` (`content_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=386 DEFAULT CHARSET=utf8 COMMENT='用户内容关联表';

-- ----------------------------
-- Table structure for cms_reply
-- ----------------------------
DROP TABLE IF EXISTS `cms_reply`;
CREATE TABLE `cms_reply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '回复（评论)id',
  `content_id` bigint(20) NOT NULL COMMENT '内容id',
  `content` longtext COMMENT '评论内容',
  `reply_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '回复时间',
  `org` varchar(255) DEFAULT NULL COMMENT '回复人所在机构名称',
  `dept_name` varchar(255) DEFAULT NULL COMMENT '回复人所在科室名称',
  `reply_name` varchar(255) DEFAULT NULL COMMENT '回复人姓名',
  `del_flag` bit(1) DEFAULT b'0' COMMENT '是否删除[0=未删除，1=删除]',
  PRIMARY KEY (`id`),
  KEY `content_id` (`content_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=213 DEFAULT CHARSET=utf8 COMMENT='回复表';

-- ----------------------------
-- Table structure for cms_reply_user
-- ----------------------------
DROP TABLE IF EXISTS `cms_reply_user`;
CREATE TABLE `cms_reply_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reply_id` bigint(20) DEFAULT NULL COMMENT '回复id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `content_id` bigint(20) DEFAULT NULL COMMENT '内容id',
  `del_flag` bit(1) DEFAULT b'0' COMMENT '是否删除[0=未1=删除]',
  PRIMARY KEY (`id`),
  KEY `reply_id` (`reply_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `content_id` (`content_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=238 DEFAULT CHARSET=utf8 COMMENT='回复用户关联表';
