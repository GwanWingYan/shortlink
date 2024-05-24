package com.nageoffer.shortlink.admin.test;

public class UserTableShardingTest {

    public static final String SQL = """
            CREATE TABLE `t_link_goto_%d` (
              `id` bigint(20) DEFAULT NULL COMMENT 'ID',
              `full_short_uri` varchar(8) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '短链接',
              `gid` varchar(32) DEFAULT 'default' COMMENT '分组标识'
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;""";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf(SQL + "%n", i);
        }
    }
}
