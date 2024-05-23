package com.nageoffer.shortlink.admin.test;

public class UserTableShardingTest {

    public static final String SQL = """
            CREATE TABLE `t_group_%d` (
              `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
              `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
              `name` varchar(64) DEFAULT NULL COMMENT '分组名称',
              `username` varchar(256) DEFAULT NULL COMMENT '创建分组用户名',
              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
              `update_time` datetime DEFAULT NULL COMMENT '修改时间',
              `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除表示: 0未删除, 1已删除',
              UNIQUE KEY `id` (`id`),
              UNIQUE KEY `idx_unique_username_gid` (`gid`,`username`) USING BTREE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;""";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf(SQL + "%n", i);
        }
    }
}
