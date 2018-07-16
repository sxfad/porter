# 配置文档
## 兼容性
```
	兼容v0.3.2+，包括v0.3.2
```
<!--
## 结构
![配置文件结构](./img/profiles_schema.png)
-->
## 文档结构

- config/application.properties

```
	spring-boot配置文件，可配置运行环境(spring.profiles.active)等，详情参考spring-boot官方文档。
	同样可通过startup.sh脚本指定上述参数,例如 startup.sh --logging.level.root=debug
```

- config/application-${env}.properties

```
	spring-boot配置文件，不同运行环境的配置文件。
```


- config/tasks/${env}/*.properties

```
	任务配置文件
	不同的环境(${env})的任务配置在config/tasks目录的不同子目录。
```


- 日志生成目录

```
	spring-boot配置参数，默认logging.file=${app.home}/logs/data-node.log。
	其中${app.home}是在启动脚本中指定的，指的是datas-node的根目录
```

## 节点编号

- node.id

```
	用于指定任务节点编号，在所属分布式环境中唯一。用于自描述并在zookeeper上报心跳、实现分布式锁。
	eg.
		node.id=100
```


## 统计
- node.statistic.upload

```
	是否上传统计信息，包括日志、TPS指标.
	类型:Boolean
```

 
## 集群
### node.cluster

```
	分布式集群实现，当前仅支持zookeeper
```



- node.cluster.strategy

```
	指定分布式集群的实现策略 
	eg.
		node.cluster.strategy=ZOOKEEPER
```

- node.cluster.client.url

```
	集群连接参数
	eg.
		node.cluster.client.url=127.0.0.1:2181
```

- node.cluster.client.sessionTimeout

```
	集群连接超时时间
	eg.
		node.cluster.client.sessionTimeout=超时时间，单位为毫秒。
```

## 告警
### node.alert

```
 	告警策略驱动，当前仅支持邮件
```

- node.alert.strategy

```
	指定告警方式
	eg.
		node.alert.strategy=EMAIL
```

- node.alert.frequencyOfSeconds

```
	相同内容通知接收频率
	eg.
		node.alert.frequencyOfSeconds=60
```

- node.alert.client

```
	node.alert.client.host=smtp服务器
	node.alert.client.username=邮箱
	node.alert.client.password=密码	
	node.alert.client.smtpAuth=true
	node.alert.client.smtpStarttlsEnable=true
	node.alert.client.smtpStarttlsRequired=false
```

- node.alert.receiver

```
	全局告警通知人
	类型：ArrayList
	node.alert.receiver[index].realName=姓名
	node.alert.receiver[index].email=邮箱
	node.alert.receiver[index].phone=手机号
```

## 公用连接池

```
	当多个任务目标端和源端共用数据库连接池时配置，用于节省数据库链接资源。
	类型:Map
	
```


### node.source.命名名字
- node.source.命名名字.sourceType

```
	源类型
	类型:enum
	可选参数:JDBC
```


- node.source.命名名字.url

```
	多个用逗号隔开
	eg.
		node.source.命名名字.url=jdbc连接
```

- node.source.命名名字.userName

```
	账户名
	eg.
		node.source.命名名字.userName=账户
```

- node.source.命名名字.password

```
	密码
	eg.
		node.source.命名名字.password=密码
```

- node.source.命名名字.maxWait
- node.source.命名名字.minPoolSize
- node.source.命名名字.maxPoolSize
- node.source.命名名字.initialPoolSize
- node.source.命名名字.connectionErrorRetryAttempts

```
	连接错误重试次数
	类型:Int
```
[//]: # (todo)
- node.source.命名名字.dbType

```
	类型:Enum
	可选参数:MYSQL、		ORACLE
```

- node.source.命名名字.makePrimaryKeyWhenNo(<font color='red'>2.0.1新增</font>)

```
	类型:Boolean
	当目标端表无主键时，默认全部字段为主键
	默认true
```



## 任务配置
###  node.task
```
	节点任务
	类型:ArrayList
```

- node.task[index].taskId

```
	任务编号
	类型:String
	eg. 
		node.task[index].taskId=1
```

- node.task[index].receiver

```
	当前任务告警通知人
	任务告警会通知到node.task[index].receiver + node.alert.receiver
	类型：ArrayList
	node.task[index].receiver[index].realName=姓名
	node.task[index].receiver[index].email=邮箱
	node.task[index].receiver[index].phone=手机号
```

- node.task[index].consumer

```
	任务消费源配置
	类型:DataConsumerConfig
```	

- node.task[index].consumer.consumerNameme	

```
		消费器插件
		类型:String
		可选择参数:CanalFetch、KafkaFetch
```

- node.task[index].consumer.converter

```
		消息转换器
		类型:String
		可选择参数:canalRow(1.0新增)、oggJson
```

- node.task[index].consumer.source

```
	消费器数据来源
	类型:Map
	
	CanalFetch：(1.0新增)
	node.task[index].consumer.source.sourceType=CANAL
	node.task[index].consumer.source.slaveId=mysql slaveId
	node.task[index].consumer.source.address=ip:port
	node.task[index].consumer.source.database=数据库名
	node.task[index].consumer.source.username=账户
	node.task[index].consumer.source.password=密码
	node.task[index].consumer.source.filter=订阅表正则
	
	KafkaFetch:
	node.task[0].consumer.source.sourceType=KAFKA
	node.task[0].consumer.source.servers=ip:port,ip:port
	node.task[0].consumer.source.topics=主题
	node.task[0].consumer.source.group=消费分组
	node.task[0].consumer.source.autoCommit=true|false
	
```

- node.task[index].consumer.metaSource


```
	元数据数据源
	类型:Map
	不做配置时，将不会做源端与目标端数据一致对比。(1.1新增规则)
	形式1：
	node.task[index].consumer.metaSource.sourceName=公共数据源名字
	
	形式2:
	node.task[index].consumer.metaSource.dbType
	node.task[index].consumer.metaSource.url
	node.task[index].consumer.metaSource.userName
	node.task[index].consumer.metaSource.password
	node.task[index].consumer.metaSource.maxWait
	node.task[index].consumer.metaSource.minPoolSize
	node.task[index].consumer.metaSource.maxPoolSize
	node.task[index].consumer.metaSource.initialPoolSize
	node.task[index].consumer.metaSource.connectionErrorRetryAttempts
```

- node.task[index].consumer.eventProcessor.className(1.1新增)

```
	自定义同步数据数据抽取器
	格式:package.className
```

- node.task[index].consumer.eventProcessor.content(1.1新增)

```
	class路径、jar路径、源码内容
```
- node.task[index].consumer.eventProcessor.emptyFetchNoticeSpan(<font color='red'>2.0.1新增</font>)

```
	空查询通知间隔,单位秒
	默认3600
```

- node.task[index].consumer.eventProcessor.emptyFetchThreshold(<font color='red'>2.0.1新增</font>)

```
	空查询通知时间阀值,单位秒
	-1时不生效，默认3600
```


- node.task[index].loader

```
	任务载入器配置
	类型:DataLoaderConfig
```

- node.task[index].loader.loaderName

```
	目标端载入器插件
	类型:Enum
	可选参数:JdbcBatch、JdbcSingle
```	

- node.task[index].loader.source


```
	目标端数据源
	类型:Map
	
	形式1：
	node.task[index].loader.source.sourceName=公共数据源名字
	
	形式2:
	node.task[index].loader.source.dbType
	node.task[index].loader.source.url
	node.task[index].loader.sourceuserName
	node.task[index].loader.source.password
	node.task[index].loader.source.maxWait
	node.task[index].loader.source.minPoolSize
	node.task[index].loader.source.maxPoolSize
	node.task[index].loader.source.initialPoolSize
	node.task[index].loader.source.connectionErrorRetryAttempts
```

- node.task[index].loader.insertOnUpdateError(<font color='red'>2.0新增</font>)

```
	目标端更新失败转插入开关参数,默认开启
	类型:Boolean
```

- node.task[index].mapper

```
	源端与目标端schema映射，用于处理源端和目标端命名不一致的情况
	类型:List
```

- node.task[index].mapper[下标].schema

```
	node.task[index].mapper[index].schema=源端schema,目标端schema
```

- node.task[index].mapper[下标].table

```
	node.task[index].mapper[index].table=源端表名,目标端表名
```


- node.task[index].mapper[下标].updateDate

```
	如果不配置或配置错误，数据同步结果检查功能不启用
	node.task[index].mapper[下标].updateDate=源端表自动更新时间字段,目标端表自动更新时间字段
```

- node.task[index].mapper[下标].column

```
	字段映射,可不配置。
	node.task[index].mapper[下标].column.源端字段名=目标端字段名
```
	