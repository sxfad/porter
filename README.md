# 数据同步工具

## 适用场景
- 最终一致
- 单向同步

## 实现依据
- MQ消息顺序消费
- MQ消息组内消息最多被消费一次
- 仅支持DML，DDL、DCL手工执行
- 表必须有主键、最后更新时间


## 优雅关闭
signalId = \`kill -l USR2\`

kill -$signalId PID  #centos:31 mac:12