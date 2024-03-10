# 大语言模型服务平台 - By 刘仕杰

基于微服务架构设计的API开放平台，支持为广大开发者提供开放API调用以及对应SDK的使用

---

>**作者**：LuckySJ-刘仕杰 - 在线体验地址[**www.ttapi.site**](https://www.ttapi.site/)

## 主要功能

1. 管理员管理接口信息，可以发布接口，上线接口，下线接口
2. 用户登录后可获取AK/SK，通过AK/SK来调用平台提供的API服务
3. 在网关对用户请求做额度扣减等统一处理，支持每日签到获取额度

## 技术栈

SpringCloud、Dubbo、Gateway、Mybatis Plus、MySQL、Redis、Redisson