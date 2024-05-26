/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `t_link_stats_today_0`;
CREATE TABLE `t_link_stats_today_0` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_1`;
CREATE TABLE `t_link_stats_today_1` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_2`;
CREATE TABLE `t_link_stats_today_2` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_3`;
CREATE TABLE `t_link_stats_today_3` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_4`;
CREATE TABLE `t_link_stats_today_4` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_5`;
CREATE TABLE `t_link_stats_today_5` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_6`;
CREATE TABLE `t_link_stats_today_6` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_7`;
CREATE TABLE `t_link_stats_today_7` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_8`;
CREATE TABLE `t_link_stats_today_8` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_9`;
CREATE TABLE `t_link_stats_today_9` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
                                        `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
                                        `date` date DEFAULT NULL COMMENT '日期',
                                        `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
                                        `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
                                        `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                        `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_10`;
CREATE TABLE `t_link_stats_today_10` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
  `date` date DEFAULT NULL COMMENT '日期',
  `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
  `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
  `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_11`;
CREATE TABLE `t_link_stats_today_11` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
  `date` date DEFAULT NULL COMMENT '日期',
  `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
  `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
  `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_12`;
CREATE TABLE `t_link_stats_today_12` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
  `date` date DEFAULT NULL COMMENT '日期',
  `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
  `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
  `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_13`;
CREATE TABLE `t_link_stats_today_13` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
  `date` date DEFAULT NULL COMMENT '日期',
  `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
  `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
  `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_14`;
CREATE TABLE `t_link_stats_today_14` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
  `date` date DEFAULT NULL COMMENT '日期',
  `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
  `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
  `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_stats_today_15`;
CREATE TABLE `t_link_stats_today_15` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '短链接',
  `date` date DEFAULT NULL COMMENT '日期',
  `today_pv` int(11) DEFAULT '0' COMMENT '今日PV',
  `today_uv` int(11) DEFAULT '0' COMMENT '今日UV',
  `today_uip` int(11) DEFAULT '0' COMMENT '今日IP数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_full-short-url` (`full_short_url`, `gid`, `date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;