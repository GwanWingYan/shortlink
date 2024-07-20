/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `t_group_0`;
CREATE TABLE `t_group_0` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_1`;
CREATE TABLE `t_group_1` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_10`;
CREATE TABLE `t_group_10` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_11`;
CREATE TABLE `t_group_11` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_12`;
CREATE TABLE `t_group_12` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_13`;
CREATE TABLE `t_group_13` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_14`;
CREATE TABLE `t_group_14` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_15`;
CREATE TABLE `t_group_15` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_2`;
CREATE TABLE `t_group_2` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_3`;
CREATE TABLE `t_group_3` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_4`;
CREATE TABLE `t_group_4` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_5`;
CREATE TABLE `t_group_5` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_6`;
CREATE TABLE `t_group_6` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_7`;
CREATE TABLE `t_group_7` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_8`;
CREATE TABLE `t_group_8` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_group_9`;
CREATE TABLE `t_group_9` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
  `user_name` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
  `sort_order` int(3) DEFAULT NULL COMMENT '分组排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `t_group_unique`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_unique_gid` (`gid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `t_group_15` (`id`, `gid`, `name`, `user_name`, `sort_order`, `create_time`, `update_time`, `del_flag`)
VALUES (1752265619253805057, 'tSUBMP', '默认分组', 'admin', 0, '2024-01-31 21:00:00', '2024-01-31 21:00:00', 0);




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;