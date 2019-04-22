# Porter | [English](./README_EN.md)

[![GitHub release](https://img.shields.io/badge/release-3.0-blue.svg)](https://github.com/sxfad/porter)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)



## 概要

Porter始于2017年，提供数据同步功能，但并不仅仅局限于数据同步，在随行付内部广泛使用。主要提供一下功能：

+ 数据库准实时同步
+ 数据库迁移
+ 数据库治理
+ 自定义源端、目标端数据同步
+ 自定义数据抽取逻辑
 



## 核心功能

+ 原生支持Oracle|Mysql到Jdbc关系型数据库最终一致同步
+ 插件友好化，支持自定义源端消费插件、目标端载入插件、告警插件等插件二次开发。
+ 支持自定义源端、目标端表、字段映射
+ 支持节点基于配置文件的同步任务配置。
+ 支持管理后台同步任务推送，节点、任务管理。提供任务运行指标监控，节点运行日志、任务异常告警。
+ 支持节点资源限流、分配。
+ 基于Zookeeper集群插件的分布式架构。支持自定义集群插件。

## 快速开始

### 从源码编译
```
git clone https://github.com/sxfad/porter.git
cd porter
git checkout 版本
gradle build
从build/distributions目录查找安装包
```

### 配置
[配置文档](https://github.com/sxfad/porter/blob/master/doc/profiles.md)

```
porter.id=节点编号，在集群中唯一

#集群配置
porter.cluster.strategy=ZOOKEEPER
porter.cluster.client.url=127.0.0.1:2181
porter.cluster.client.sessionTimeout=100000

#统计指标数据上传目标端链接信息，与manager-cluster搭配使用,当上传到zookeeper时，manager-cluster无需部署。
porter.cluster.statistic.sourceType=KAFKA_PRODUCE
porter.cluster.statistic.servers=127.0.0.1:9200
porter.cluster.statistic.topic=你的kafka主题


#单机配置
porter.cluster.strategy=STANDALONE
porter.cluster.client.home=./.porter
```

### 运行
```
tar zxvf build/distributions/porter-boot-版本.tar
porter-boot-版本/bin/porter-boot
```

### 调试
```
porter-boot-版本/bin/porter-boot  debug 端口号
```
### 运行环境
```
porter-boot-版本/bin/porter-boot --spring.profiles.active=运行环境
```
### 关闭
```
porter-boot-版本/bin/shutdown.sh
```


### 强制启动
```
当porter-boot因jvm crash、kill -9强杀进程等原因造成节点、任务没有正常退出，再次启动porter-boot不成功、任务无法分配时使用。
通过porter-boot的http接口http://ip:端口/inspect/node/info  查看参数"forceAssign"判断是否处于强制启动状态
porter-boot-版本/bin/porter-boot --force
```

## 文档
+ [部署教程 3.0.2](./doc/arrange.md)
+ [部署教程@code-hipster](./doc/tutorial.md)
+ [中文文档](./doc/document.md)
+ [英文文档](./doc/document_EN.md)

## 架构设计
![架构设计](doc/img/architecture.png)
![数据流](doc/img/workflow.png)

## 快速预览

![首页](doc/img/Home.png)
+ [管理员手册](./doc/manager_manual.md)

## 联系我们

* QQ群：835209101

![首页](doc/img/wx.png)
