#统计分析
- 节点状态监控

```
{
    "address": "IP",
    "heartbeat": "节点心跳时间",
    "hostName": "主机名",
    "nodeId": "节点ID",
    "processId": "进程号",
    "status": "节点状态:SUSPEND、WORKING",
    "tasks": {
        "任务ID": [
            "泳道ID"
        ]
    }
}
```

- 任务状态监控

```
{
    "alertedTimes": 告警次数,
    "deleteRow": 删除行数,
    "errorDeleteRow": 删除错误行数,
    "errorInsertRow": 插入错误行数,
    "errorUpdateRow": 更新错误行数,
    "insertRow": 插入行数,,
    "updateRow": 更新行数,
    "heartbeatTime": "心跳时间",
    "lastLoadedSystemTime": "最近载入系统时间",
    "nodeId": "节点ID",
    "progress": "任务进度",
    "registeredTime": "注册时间",
    "schema": "schena",
    "swimlaneId": "泳道",
    "table": "表",
    "taskId": "任务ID"
}
```

- TPS统计

```
{
    "alertedTimes": 告警次数,
    "deleteRow": 删除行数,
    "errorDeleteRow": 删除错误行数,
    "errorInsertRow": 插入错误行数,
    "errorUpdateRow": 更新错误行数,
    "insertRow": 插入行数,,
    "updateRow": 更新行数
    "nodeId": "节点ID",
    "time": "yyyyMMddHHmm",
    "schema": "schena",
    "swimlaneId": "泳道",
    "table": "表",
    "taskId": "任务ID"
}
```

- 日志上报

```
{
	"type":"日志类型 taskStopAlarm、taskLog",
    "error": "日志内容",
    "address": ip地址,
    "hostName": 主机名,
    "processId": 进程号,
    "time": "时间 yyyyMMddHHmmss",
    "swimlaneId": "泳道ID",
    "taskId": "任务ID",
}
```

- 事件上报

```
	节点上下线、任务出错停止等
```

- <font color='red'>[2.0.1新增]</font>状态自检

```
	http://ip:port/inspect/node/error 任务异常信息
	http://ip:port/inspect/node/info  节点状态
	http://ip:port/inspect/node/jinfo  jvm jinfo
	http://ip:port/inspect/node/jmap  jvm jmap
	http://ip:port/inspect/node/jstat  jvm jstat
	http://ip:port/inspect/node/jstack  jvm jstack
```