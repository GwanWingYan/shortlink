/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

DROP TABLE IF EXISTS `t_locale_stats`;
CREATE TABLE `t_locale_stats`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `lid`         varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接标识符',
    `date`        date                                           DEFAULT NULL COMMENT '日期',
    `cnt`         int(11)                                        DEFAULT NULL COMMENT '访问量',
    `province`    varchar(64) CHARACTER SET utf8mb4              DEFAULT NULL COMMENT '省份名称',
    `city`        varchar(64) CHARACTER SET utf8mb4              DEFAULT NULL COMMENT '市名称',
    `adcode`      varchar(64) CHARACTER SET utf8mb4              DEFAULT NULL COMMENT '城市编码',
    `country`     varchar(64) CHARACTER SET utf8mb4              DEFAULT NULL COMMENT '国家编码',
    `create_time` datetime                                       DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                       DEFAULT NULL COMMENT '修改时间',
    `delete_flag` tinyint(1)                                     DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
    UNIQUE KEY `id` (`id`),
    UNIQUE KEY `idx_unique_locale_stats` (`lid`, `date`, `adcode`, `province`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4;



/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;