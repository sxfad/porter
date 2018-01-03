# 数据同步工具

## 适用场景
- 最终一致
- 单向同步

## 实现依据
- MQ消息顺序消费
- MQ消息组内消息最多被消费一次
- 仅支持DML，DDL、DCL手工执行
- 表必须有主键、最后更新时间

## 一期架构
![Node节点内存模型](./docs/images/数据同步一期功能列表.png)

## 远景架构
![Node节点内存模型](./docs/images/远景架构.png)
![Node节点内存模型](./docs/images/功能结构.png)

## Node节点内存模型
![Node节点内存模型](./docs/images/node-model.png)
>
TaskController 1---* TaskWorker <br/>
TaskWorker 1---* TaskWork <br/>
TaskWork 1---1 *Job <br/>
通俗上讲TaskController对应Node进程，进程内只有一个；TaskWorker对应任务，每个任务对应一个Worker；每个任务有多个管道，即TaskWork,对应MQ topic；每个Work有多个阶段性任务。整个来讲这就是一个管道过滤器风格的架构模式。 <br/>
SelectJob单线程从数据源消费数据 <br/>
ExtractJob单线程从Select队列中读取数据，多线程提取数据 <br/>
TrasnformJob单线程从Extract内存集合中读取数据，多线程映射转换数据 <br/>
LoadJob单线程按照SelectJob消费顺序加载数据到数据库 <br/>
AlertJob单线程同步Zookeeper数据库检查时间点，对比指定时间段内源数据库和目标数据库的数据条目差异，按照配置文件配置的告警方式进行告警 <br/>

## OGG部署结构
![Node节点内存模型](./docs/images/ogg.png)


## 问题&方案(现象描述与补偿)
- MQ分发问题
    - 不同的源数据库吐到不同的MQ集群
    - broker集群存储,主题仅允许唯一分区。
    - 每张表单独对应一个主题
    - 消息消费节点集群分主题消费
    - 消息消费节点仅消费消息，无复杂业务逻辑
    - 人工控制消息生产进度
- 数据一致性问题
    - 最终一致性
    - 消息丢失
        - target record和source  record不一致
            - 最后一次插入、更新消息丢失
                - 目标库落后源库
            - 最后一次删除消息丢失
                - 目标库冗余脏数据
        - 消息条件匹配不到目标库
            - 没有插入消息，只有更新、删除消息
                - 根据最新值插入
                - 无需删除动作
            - 主键变化条件匹配
                - 根据主键更新最新值
            - 主键变化条件不匹配
                - 目标库插入新主键，旧主键冗余脏数据
    - 消息丢失解决方案
        - 查询最近未发生变化的数据记录
            - 随着时间的推移，数据集是发生变化的，没办法根据时间进行任务分片，也不能累积历史任务执行结果。
        - 查询指定时间发生变化的数据记录
            - 记录最初开始同步时间
            - 按照合理时间跨度，比对当前时间节点前指定时间跨度的目标库和源库数据记录
            - 累计已完成数据比对时间
	
## quick start
git clone http://192.168.120.68/zhangkewei/suixingpay-datas.git

cd suixingpay-datas

gradle build

tar zxvf build/distributions/node-boot-1.0-SNAPSHOT.tar

node-boot-1.0-SNAPSHOT/bin/node-start

## 优雅关闭
signalId = \`kill -l USR2\`

kill -$signalId PID  #centos:31 mac:12

