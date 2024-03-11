# TT API开放平台 - By 刘仕杰

项目介绍：为广大开发者提供API接口调用的平台，用户注册登录后，
可通过url或者提供的SDK使用平台的接口服务。管理员可通过后台管理接口信息，
并查看接口的调用情况。使用API网关实现了对调用请求的统一处理，如路由转发，鉴权，服务保护，调用计数等功能。

---

>**作者**：LuckySJ-刘仕杰 - 在线演示地址 [**www.luckylottery.site**](www.luckylottery.site)

## 运行配置

- 运行环境：JDK 1.8+
- Springboot：2.7.2
- mysql：8.2
- redis：5.0

## 实现功能

- 用户注册登录后可使用自己的AK/SK调用平台提供的开放API服务
- 用户可以通过邀请注册，签到等任务获取调用额度
- 用户可在接口界面查看接口的相关信息并在线调试
- 管理员可以发布，上线，下线接口，目前接口只有一个天气接口，请自行扩展

## 技术栈

- SpringCloud、Dubbo、Gateway、Nacos、Mybatis Plus、Redis、Mysql


