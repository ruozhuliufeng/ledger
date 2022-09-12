# 分类账簿记录

## 介绍

​	Ledger-一款简单好用的记账系统,支持手动记账及第三方支付(当前支持微信、支付宝)账单导入，基于SpringSecurity权限控制,保证数据安全。提供报表分析及一体化驾驶舱，全方位的分析您的财务状况。

## 项目地址

|       名称        |                    地址                     |
| :---------------: | :-----------------------------------------: |
|   ledger-github   |   https://github.com/ruozhuliufeng/ledger   |
|   ledger-gitee    |   https://gitee.com/ruozhuliufeng/ledger    |
| ledger-vue-github | https://github.com/ruozhuliufeng/ledger-vue |
| ledger-vue-gitee  | https://gitee.com/ruozhuliufeng/ledger-vue  |

## 环境

- JDK:JDK 1.8
- SpringBoot:2.6.10
- SpringSecurity:2.6.10
- MySQL:5.7
- JJWT:0.9.1
- Mybatis-Plus：3.4.1
- Maven：3.6.3

## 预览

> 访客账号：guest 访客密码：123654
>
> 请注意：访客账号的交易记录信息会在凌晨2点清空，仅供演示

​		预览地址：[ledger](http://finance.aixuxi.cn/login)

## 文档说明(编写中)

- CSDN：[ledger](https://blog.csdn.net/ruozhuliufeng/category_11964259.html?spm=1001.2014.3001.5482)
- 微信公众号：若竹流风

## 实现功能

- 简单记一笔
- 账单报表分析
- 交易记录
- 支付宝/微信账单导入
- 加入家庭
- 家庭成员交易记录信息
- 。。。。

### 运行

​	 RUN  LedgerApplication

## 开发者说

1. 感谢 **Jetbrains** 为开源项目提供的 License

​			<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" alt="Jetbrains" height="150"/>    

2. 后续工作开发

- [x] 首页报表处理
- [x] 头像上传至SM.MS
  - SM.MS API说明文档：[doc](https://doc.sm.ms/)
- [x] 每日0点备份数据库(已完成,未启用)
- [ ] 备份数据库文件保存至网盘(webdav传输)
- [ ] 家庭记账优化
  - 同一用户仅可创建或加入一个家庭
- [ ] 目标管理
  - 个人目标管理
  - 家庭目标管理
- [ ] 驾驶舱
  - 大屏设计与显示
- [ ] 团队记账功能
  - 同一用户可加入多个团队
  - 可切换团队显示
- [ ] 小程序端开发
3. 请开发者喝杯咖啡

<img src="http://media.aixuxi.cn/weixinzhifu.jpg" alt="微信支付" style="zoom:20%;" />
