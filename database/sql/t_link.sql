/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `t_link`;
CREATE TABLE `t_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
  `full_short_url` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '原始链接',
  `click_num` int(11) DEFAULT NULL COMMENT '点击量',
  `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识: 0启用, 1未启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型: 0接口创建, 1控制台创建',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型: 0永久有效, 1自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识: 0未删除, 1已删除',
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `idx_unique_full_short_url` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

INSERT INTO `t_link` (`id`, `domain`, `short_uri`, `full_short_url`, `origin_url`, `click_num`, `gid`, `enable_status`, `created_type`, `valid_date_type`, `valid_date`, `describe`, `create_time`, `update_time`, `del_flag`) VALUES
(1, 'http://nrul.ink', 'skxj3u', 'http://nrul.ink/skxj3u', 'https://nageoffer.com/', 0, 'default', 0, 1, 0, NULL, '测试短链接', '2024-05-23 04:51:33', '2024-05-23 04:51:33', 0);


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;