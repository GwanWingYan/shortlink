# Shortlink: 短链接 SaaS 系统


## 简介

短链接是指将一个原始 URL 通过特定的算法或服务转化为一个更短、易于记忆的 URL。
短链接通常只包含几个字符，而原始 URL 的长度则是任意的。

简单来说，Shortlink 系统的原理是：
1. **生成唯一标识符 (code)**：当用户输入或提交一个原始 URL 时，短链接服务会生成一个唯一标识符。
2. **关联标识符与原始 URL**：短链接服务将这个唯一标识符与用户提供的长 URL 关联起来，并将其保存在数据库或者其他持久化存储中。
3. **创建短链接**：将生成的唯一标识符加上短链接服务的域名（例如：http://shortl.ink ）作为前缀，构成一个短链接。
4. **重定向**：当用户访问该短链接时，短链接服务接收到请求后会根据唯一标识符查找关联的原始 URL，然后将用户重定向到这个原始 URL。
5. **跟踪统计**：一些短链接服务还会提供访问统计和分析功能，记录访问量、来源、地理位置等信息。

## 技术栈

Shortlink 后端技术栈如下：

* JDK 17
* Spring Boot 3
* Spring Cloud Gateway
* Redis Cluster 缓存集群
* MySQL 数据库读写分离
* ShardingSphere
* Nacos 服务注册发现和分布式配置
* SkyWalking Tracing 分布式追踪
* Sentinel 服务熔断和限流
* XXL-JOB 定时任务
* Docker 容器打包
* Jenkins CI/CD
* Docker Compose / K8S 部署


## 系统设计

Shortlink 采用微服务架构，分为四大微服务:

* **短链接网关服务 (gateway)**：作为沟通前端控制台和后端（即管理服务和核心服务）的渠道，基于 Spring Cloud Gateway 对前端请求进行分流，并通过 Spring Cloud Loadbalancer 的负载均衡算法转发到相应的管理服务和核心服务实例上。
* **短链接管理服务 (admin)**：负责对来自网关服务的请求进行身份鉴权与流量控制，并提供用户管理和短链接分组管理功能，具有以下特点：
  * 使用布隆过滤器查询短链接或短链接分组是否存在，避免直接访问数据库导致的缓存穿透。
  * 使用 ShardingSphere 对数据库进行分表，使数据库支持水平扩展。
  * 使用 Redis 分布式互斥锁，高效保障创建分组业务的原子性。
  * 借助 Redis Lua 脚本的原子性特点，实现对用户请求的流量控制。
  * 使用 OpenFeign 调用短链接核心服务的 RESTful API。
* **短链接核心服务 (core)**
* **短链接前端服务 (console)**

Shortlink 采用 Nacos 作为服务注册和发现中心，各微服务实例在启动时将自身注册到 Nacos 中，并通过 Nacos 获取其他微服务实例列表。


## TODO

* core 重构待办事项：
  * 重构数据库表 
    * ~~去除 domain 字段~~
    * ~~将 domain 作为全局配置项~~
    * ~~重构 short_uri 为 lid~~
    * 删除 full_short_url
    * 重构其他字段名字
  * 重构 URL
* 厘清 admin 和 core 的职责
* 测试网关
* 在 README 添加运行指南 
