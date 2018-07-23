# Access process
	
## Prerequisites
```
	The middleware can make a custom mapping of table fields, table names, and schema names, and supports inconsistent default conversion of source and destination end table field data types, and supports field filtering;
	Support for custom business logic by implementing interfaces (already the demand side is doing custom business logic work).
```
		
## Prepare
### Mysql to relational database synchronization
```
	Source:
		Table structure information:
			schema name
			table name
			When the source targets are inconsistent, a mapping relationship is required.
			Primary key (required)
			pdate time field (not required, if there is no source, the number of data on the target side is checked)
		bin-log:
			ROW format
		Database connection information:
			It is not required. If it is not provided, the source and destination data will not be checked.
		Data extraction connection information:
			Mysql account with replication privileges
    Target:
    	Table structure information : same as Source
    	Target database connection information	
    	If it is mysql, it is not case sensitive (temporarily not case sensitive)
```
[//]: # (todo)
### Oracle到关系型数据库同步

```
	源端:
		运维配合做OGG配置
		表结构信息:
			schema名
			表名
			源端目标端不一致时，需提供映射关系
			主键（必须）
			更新时间字段(非必填，如果没有将不做源端、目标端数据条数一致检查)
		数据库连接信息:
			非必填，如果不提供将不做源端、目标端数据条数一致检查
    目标端:
    	表结构信息:同源端
    	目标端数据库连接信息
    	如果是mysql,要求不区分大小写(暂不支持大小写敏感)
		
```

### Custom sync
```
	See the plugin development chapter for details.
```


## Process
```
	Involving custom business logic, it is necessary to advance the docking plug development work (developed by the demand side) to audit the custom logic code in the multi-threaded concurrent environment;
	If the custom business logic is not involved, the deployment period is completed within 1 day. The time is uncontrollable due to parameter synchronization.
```

## Environmental deployment

### Development environment
```
	Use the hardware resources of the demand side to assist in deployment.
```
	

### test environment
```
	In a short period of time, the existing hardware can carry the test available architecture resources, and use up the destruction.
```

### Production Environment
```
	Unified deployment, separately applying hardware resources to access data synchronization clusters.
	Can be deployed separately if there are special needs.
```

### Business development support
```
	Help documentation:
		See the plugin development chapter.
```

