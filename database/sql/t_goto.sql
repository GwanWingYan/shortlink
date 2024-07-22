/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

DROP TABLE IF EXISTS `t_goto_0`;
CREATE TABLE `t_goto_0`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_1`;
CREATE TABLE `t_goto_1`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_10`;
CREATE TABLE `t_goto_10`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_11`;
CREATE TABLE `t_goto_11`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_12`;
CREATE TABLE `t_goto_12`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_13`;
CREATE TABLE `t_goto_13`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_14`;
CREATE TABLE `t_goto_14`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_15`;
CREATE TABLE `t_goto_15`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_2`;
CREATE TABLE `t_goto_2`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_3`;
CREATE TABLE `t_goto_3`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_4`;
CREATE TABLE `t_goto_4`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_5`;
CREATE TABLE `t_goto_5`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_6`;
CREATE TABLE `t_goto_6`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_7`;
CREATE TABLE `t_goto_7`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_8`;
CREATE TABLE `t_goto_8`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `t_goto_9`;
CREATE TABLE `t_goto_9`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gid` varchar(32)                                    DEFAULT 'default' COMMENT '分组标识',
    `lid` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_lid` (`lid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;