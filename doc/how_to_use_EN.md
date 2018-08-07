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
			Update time field (not required, if there is no source, the number of data on the target side is checked)
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

### Oracle to relational database synchronization

```
	Source:
		Operation and Maintenance Engineer cooperate with OGG configuration.
		Table structure information:
			schema name
            table name
			When the source targets are inconsistent, a mapping relationship is required.
			Primary key (required)
			Update time field (not required, if there is no source, the number of data on the target side is checked)
		Database connection information:
			It is not required. If it is not provided, the source and destination data will not be checked.
    Target:
    	Table structure information : same as Source
    	Target database connection information	
    	If it is mysql, it is not case sensitive (temporarily not case sensitive)
		
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

