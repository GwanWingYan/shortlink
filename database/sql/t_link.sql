/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `t_link_0`;
CREATE TABLE `t_link_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_1`;
CREATE TABLE `t_link_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_10`;
CREATE TABLE `t_link_10` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_11`;
CREATE TABLE `t_link_11` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_12`;
CREATE TABLE `t_link_12` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_13`;
CREATE TABLE `t_link_13` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_14`;
CREATE TABLE `t_link_14` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_15`;
CREATE TABLE `t_link_15` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_2`;
CREATE TABLE `t_link_2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_3`;
CREATE TABLE `t_link_3` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_4`;
CREATE TABLE `t_link_4` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_5`;
CREATE TABLE `t_link_5` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_6`;
CREATE TABLE `t_link_6` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_7`;
CREATE TABLE `t_link_7` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_8`;
CREATE TABLE `t_link_8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `t_link_9`;
CREATE TABLE `t_link_9` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1793536276738203651 DEFAULT CHARSET=utf8mb4;


































/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;