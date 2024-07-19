# Shortlink: 短链接 SaaS 系统


## 简介

短链接是指将一个原始 URL 通过特定的算法或服务转化为一个更短、易于记忆的 URL。
短链接通常只包含几个字符，而原始 URL 的长度则是任意的。

简单来说，Shortlink 系统的原理是：
1. **生成唯一标识符**：当用户输入或提交一个原始 URL 时，短链接服务会生成一个唯一标识符。
2. **关联标识符与原始 URL**：短链接服务将这个唯一标识符与用户提供的长 URL 关联起来，并将其保存在数据库或者其他持久化存储中。
3. **创建短链接**：将生成的唯一标识符加上短链接服务的域名（例如：http://shortl.ink ）作为前缀，构成一个短链接。
4. **重定向**：当用户访问该短链接时，短链接服务接收到请求后会根据唯一标识符查找关联的原始 URL，然后将用户重定向到这个原始 URL。
5. **跟踪统计**：一些短链接服务还会提供访问统计和分析功能，记录访问量、来源、地理位置等信息。

    

## 底层技术

Shortlink 采用以下底层技术：

* JDK 17
* Spring Boot 3
* Spring Cloud Gateway
* Redis Cluster 缓存集群
* MySQL 数据库读写分离
* Nacos 服务注册发现和分布式配置
* SkyWalking Tracing 分布式追踪
* Sentinel 服务熔断和限流
* XXL-JOB 定时任务
* Docker 容器打包
* Jenkins CI/CD
* Docker Compose / K8S 部署

## TODO

* 系统设计图
  * 四大微服务：
    * 后端
      * 管理 admin
      * 核心 project
      * 网关 gateway
      * 前端控制台 console-vue
    * 各个微服务的
       * 功能
       * API
       * 关联
* 运行 
