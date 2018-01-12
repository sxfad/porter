# 配置

### 节点编号
>
 node.id=1
 
### 集群
> 
cluster.driver.type=ZOOKEEPER <br/>
cluster.driver.url=127.0.0.1:2181 <br/>
cluster.driver.extendAttr.sessionTimeout=超时时间，单位为毫秒。

### 告警
>
alert.driver.alertWay=EMAIL <br/>
alert.driver.extend.reciver=收件人邮箱，多个用“,”分割 <br/>
alert.driver.extend.addresser=发件人邮箱 <br/>

### 邮件
>
当alert.driver.alertWay=EMAIL时配合使用<br/>
spring.mail.host=smtp地址<br/>
spring.mail.username=邮箱<br/>
spring.mail.password=密码<br/>
spring.mail.properties.mail.smtp.auth=true<br/>
spring.mail.properties.mail.smtp.starttls.enable=true<br/>
spring.mail.properties.mail.smtp.starttls.required=false<br/>

### 公用连接池
>
dataDriver.drivers.名字<br/>
dataDriver.drivers.名字.type=ORACLE<br/>
dataDriver.drivers.名字.url=jdbc连接<br/>
dataDriver.drivers.名字.userName=账户<br/>
dataDriver.drivers.名字.password=密码<br/>
dataDriver.drivers.名字.extendAttr.maxWait=30000<br/>
dataDriver.drivers.名字.extendAttr.minPoolSize=20<br/>
dataDriver.drivers.名字.extendAttr.maxPoolSize=20<br/>
dataDriver.drivers.名字.extendAttr.initialPoolSize=5<br/>

### 任务配置
>
- task.items[下标]任务数组,下标从0开始
 <br/>
task.items[下标].taskId=1<br/>
task.items[下标].loader=single或batch<br/>
task.items[下标].sourceDriver.name=源端数据库名(公用连接池名字)<br/>
task.items[下标].targetDriver.type=ORACLE<br/>
task.items[下标].targetDriver.url=jdbc连接<br/>
task.items[下标].targetDriver.userName=账户<br/>
task.items[下标].targetDriver.password=密码<br/>
task.items[下标].targetDriver.extendAttr.maxWait=30000<br/>
task.items[下标].targetDriver.extendAttr.minPoolSize=20<br/>
task.items[下标].targetDriver.extendAttr.maxPoolSize=20<br/>
task.items[下标].targetDriver.extendAttr.initialPoolSize=5<br/>
task.items[下标].dataDriver.type=KAFKA<br/>
task.items[下标].dataDriver.url=kafka broker集群列表<br/>
task.items[下标].dataDriver.extendAttr.topic=topic,多个用“，”分割<br/>
task.items[下标].dataDriver.extendAttr.group=消费者组<br/>
task.items[下标].dataDriver.extendAttr.converter=ogg(消息转换器，目前仅有ogg格式实现)<br/>
- task.items[下标].mappers[映射器下标] 映射器数组<br/>
task.items[下标].mappers[映射器下标].schema=源端schema,目标端schema<br/>
task.items[下标].mappers[映射器下标].table=源端表名,目标端表名<br/>
[^脚注]:如果不配置或配置错误，数据同步结果检查功能不启用<br/>
task.items[下标].mappers[映射器下标].updateDate=源端表自动更新时间字段,目标端表自动更新时间字段<br/>
[^脚注]:可不配置<br/>
task.items[下标].mappers[映射器下标].columns.源端字段名=目标端字段名<br/>