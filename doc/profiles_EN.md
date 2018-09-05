# Configuration document
## compatibility
```
	compatible v0.3.2+，include v0.3.2
```

## Document structure

- config/application.properties

```
	Spring-boot configuration file, configurable running environment (spring.profiles.active), etc. For details, please refer to the spring-boot official documentation.
	The above parameters can also be specified via the startup.sh script, for example: startup.sh --logging.level.root=debug
```

- config/application-${env}.properties

```
	Spring-boot configuration file, configuration files for different runtime environments.
```


- config/tasks/${env}/*.properties

```
	Task configuration file
	Tasks for different environments (${env}) are configured in different subdirectories of the config/tasks directory.
```


- Log generation directory

```
	spring-boot configuration parameters，default : logging.file=${app.home}/logs/data-node.log.
	${app.home} is specified in the startup script and refers to the root directory of datas-node.
```

## Node number

- node.id

```
	Used to specify the task node number, which is unique in the distributed environment. Used to self-describe and report heartbeats on zookeeper to implement distributed locks.
	eg.
		node.id=100
```


## Statistic
- node.statistic.upload

```
	Whether to upload statistics, including logs, TPS indicators.
	Type : Boolean
```

 
## Cluster
### node.cluster

```
	Distributed cluster implementation, currently only supports zookeeper
```



- node.cluster.strategy

```
	Specify the implementation strategy of the distributed cluster.
	eg.
		node.cluster.strategy=ZOOKEEPER
```

- node.cluster.client.url

```
	Cluster connection parameters.
	eg.
		node.cluster.client.url=127.0.0.1:2181
```

- node.cluster.client.sessionTimeout

```
	Cluster connection timeout
	eg.
		node.cluster.client.sessionTimeout=overtime time, in milliseconds.
```

## Alert
### node.alert

```
 	Alarm policy driver, currently only supports mail.
```

- node.alert.strategy

```
	Specify the alarm mode
	eg.
		node.alert.strategy=EMAIL
```

- node.alert.frequencyOfSeconds

```
	Same content notification receiving frequency.
	eg.
		node.alert.frequencyOfSeconds=60
```

- node.alert.client

```
	node.alert.client.host=smtp server
	node.alert.client.username=mail address
	node.alert.client.password=password
	node.alert.client.smtpAuth=true
	node.alert.client.smtpStarttlsEnable=true
	node.alert.client.smtpStarttlsRequired=false
```

- node.alert.receiver

```
	Global alarm informer
	Type ：ArrayList
	node.alert.receiver[index].realName=name
	node.alert.receiver[index].email=mail address
	node.alert.receiver[index].phone=phone number
```

## Public connection pool

```
	Configured when multiple task targets and sources share a database connection pool to save database link resources.
	Type : Map
	
```


### node.source.Named name
- node.source.Named name.sourceType

```
	Source type
	Type : enum
	Optional parameter : JDBC
```


- node.source.Name name.url

```
	Multiple separated by ","
	eg.
		node.source.Named name.url=jdbc link
```

- node.source.Named name.userName

```
	account name
	eg.
		node.source.命名名字.userName=account name
```

- node.source.Named name.password

```
	password
	eg.
		node.source.Named name.password=password
```

- node.source.Named name.maxWait
- node.source.Named name.minPoolSize
- node.source.Named name.maxPoolSize
- node.source.Named name.initialPoolSize
- node.source.Named name.connectionErrorRetryAttempts

```
	Connection error retries
	Type : Int
```
[//]: # (todo)
- node.source.Named name.dbType

```
	Type : Enum
	Optional parameters : MYSQL、		ORACLE
```

- node.source.Connection error retries.makePrimaryKeyWhenNo(<font color='red'>2.0.1 add</font>)

```
	Type : Boolean
	When the target table has no primary key, the default all fields are primary keys.
	default : true
```



## Task configuration
###  node.task
```
	Node task
	Type : ArrayList
```

- node.task[index].taskId

```
	Task number
	Type : String
	eg. 
		node.task[index].taskId=1
```

- node.task[index].receiver

```
	Current task alarm informer
	Task alert will be notified : node.task[index].receiver + node.alert.receiver
	Type ：ArrayList
	node.task[index].receiver[index].realName=name
	node.task[index].receiver[index].email=mail address
	node.task[index].receiver[index].phone=phone number
```

- node.task[index].consumer

```
	Task consumption source configuration
	Type : DataConsumerConfig
```	

- node.task[index].consumer.consumerNameme	

```
		Consumer plugin
		Type : String
		Optional parameter : CanalFetch、KafkaFetch
```

- node.task[index].consumer.converter

```
		Message converter
		Type : String
		Optional parameter : canalRow(1.0 add)、oggJson
```

- node.task[index].consumer.source

```
	Consumer data source
	Type : Map
	
	CanalFetch：(1.0 add)
	node.task[index].consumer.source.sourceType=CANAL
	node.task[index].consumer.source.slaveId=mysql slaveId
	node.task[index].consumer.source.address=ip:port
	node.task[index].consumer.source.database=database
	node.task[index].consumer.source.username=account
	node.task[index].consumer.source.password=password
	node.task[index].consumer.source.filter=Subscription form regular
	
	KafkaFetch:
	node.task[0].consumer.source.sourceType=KAFKA
	node.task[0].consumer.source.servers=ip:port,ip:port
	node.task[0].consumer.source.topics=topic
	node.task[0].consumer.source.group=Consumer group
	node.task[0].consumer.source.autoCommit=true|false
	
```

- node.task[index].consumer.metaSource


```
	Metadata data source
	Type : Map
	When the configuration is not done, the data between the source and the target will not be compared.(1.1 new rule)
	Form 1：
	node.task[index].consumer.metaSource.sourceName=public data source name
	
    Form 2:
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

- node.task[index].consumer.eventProcessor.className(1.1 add)

```
	Custom sync data data extractor
	Format : package.className
```

- node.task[index].consumer.eventProcessor.content(1.1 add)

```
	class path、jar path、Source content
```
- node.task[index].consumer.eventProcessor.emptyFetchNoticeSpan(<font color='red'>2.0.1 add</font>)

```
	Empty query notification interval, in seconds.
	default : 3600
```

- node.task[index].consumer.eventProcessor.emptyFetchThreshold(<font color='red'>2.0.1 add</font>)

```
	Empty query notification time threshold, in seconds
	-1 does not take effect, default 3600
```


- node.task[index].loader

```
	Task loader configuration
	Type : DataLoaderConfig
```

- node.task[index].loader.loaderName

```
	Target loader plugin
	Type : Enum
	Optional parameter : JdbcBatch、JdbcSingle
```	

- node.task[index].loader.source


```
	Target data source
	Type : Map
	
	Form 1：
	node.task[index].loader.source.sourceName=public data source name
	
	Form 2:
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

- node.task[index].loader.insertOnUpdateError(<font color='red'>2.0 add</font>)

```
	The target end update fails to insert the switch parameter, default enabled.
	Type : Boolean
```

- node.task[index].mapper

```
	Source and destination schema mapping, used to handle the inconsistency between source and destination naming.
	Type : List
```

- node.task[index].mapper[subscript].schema

```
	node.task[index].mapper[index].schema=source schema,target schema
```

- node.task[index].mapper[subscript].table

```
	node.task[index].mapper[index].table=source table name, target table name
```


- node.task[index].mapper[subscript].updateDate

```
	Data synchronization result check function is not enabled if it is not configured or configured incorrectly.
	node.task[index].mapper[subscript].updateDate=The source table automatically updates the time field, and the target table automatically updates the time field.
```

- node.task[index].mapper[subscript].column

```
	Field mapping, no need to configure.
	node.task[index].mapper[subscript].column.Source field name = target field name
```