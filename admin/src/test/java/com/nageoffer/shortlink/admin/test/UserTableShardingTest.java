package com.nageoffer.shortlink.admin.test;

public class UserTableShardingTest {

    public static final String SQL = """
            CREATE TABLE `t_link_%d` (
              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
              `domain` varchar(128) DEFAULT NULL COMMENT '域名',
              `short_uri` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短链接',
              `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
              `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
              `click_num` int(11) DEFAULT NULL COMMENT '点击量',
              `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识',
              `favicon` varchar(256) DEFAULT NULL COMMENT '网站图标',
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;""";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf(SQL + "%n", i);
        }
    }
}
