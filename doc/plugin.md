# 插件开发

## 插件开发
- 消费器插件

```
继承AbstractDataConsumer类

通过spring.factories发布:

com.suixingpay.datas.node.core.consumer.DataConsumer=xxx

通过配置文件生效:

node.task[index].consumer.consumerName=pluginName

```

- EventConverter(消费源消息转换器插件)

```
通过spring.factories发布:

com.suixingpay.datas.node.core.event.s.EventConverter = \
  com.suixingpay.datas.node.plugin.OggXmlConverter

通过配置文件生效:

node.task[index].consumer.converter=oggXml
```

- EventProcessor(自定义消费数据抽取插件)

```
可以以jar、class、源码的形式编写

通过配置文件生效：

node.task[index].consumer.eventProcessor.className=com.suixingpay.datas.node.plugin.CustomEventProcessor
node.task[index].consumer.eventProcessor.content=文件路径

```

- 载入器插件


```
继承AbstractDataLoader类

通过spring.factories发布:

com.suixingpay.datas.node.core.loader.DataLoader=xxx

通过配置文件生效:

node.task[index].loader.loaderName=pluginName

```

- 告警插件

```
暂不开放
```






## 插件注册

```
	放置到node-boot-version/plugins目录下，重启node-boot后生效。
```


## 开发样例
[demo](http://192.168.120.68/root/suixingpay-datas-plugin-sample)


