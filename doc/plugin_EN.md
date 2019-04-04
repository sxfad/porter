# Plugin development

## Plugin development
- Consumer plugin

```
Extends AbstractDataConsumer class

Published by spring.factories:

cn.vbill.middleware.porter.core.task.consumer.DataConsumer=xxx

Effective through the configuration file:

porter.task[index].consumer.consumerName=pluginName

```

- EventConverter(Consumer Source Message Converter Plugin)

```
Published by spring.factories:

cn.vbill.middleware.porter.core.message.converter.EventConverter = \
  cn.vbill.middleware.porter.plugin.OggXmlConverter

Effective through the configuration file:

porter.task[index].consumer.converter=oggXml
```

- EventProcessor(Custom consumption data extraction plugin)

```
Can be written in the form of jar, class, source code

Effective through the configuration file:

porter.task[index].consumer.eventProcessor.className=cn.vbill.middleware.porter.plugin.CustomEventProcessor
porter.task[index].consumer.eventProcessor.content=file path

```

- Loader plugin


```
Extends AbstractDataLoader class

Published by spring.factories:

cn.vbill.middleware.porter.core.loader.DataLoader=xxx

Effective through the configuration file:

porter.task[index].loader.loaderName=pluginName

```

- Alarm plugin

```
Not open yet
```






## Plugin registration

```
	Place it in the porter-boot-version/plugins directory and take effect after restarting porter-boot.
```


## Development example
[demo](http://192.168.120.68/root/suixingpay-datas-plugin-sample)


