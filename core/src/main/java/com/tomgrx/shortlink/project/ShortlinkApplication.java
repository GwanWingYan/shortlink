package com.tomgrx.shortlink.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.tomgrx.shortlink.project.dao.mapper")
public class ShortlinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkApplication.class, args);
    }
}
