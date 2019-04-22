# Porter | [中文](./README.md)

[![GitHub release](https://img.shields.io/badge/release-3.0-blue.svg)](https://github.com/sxfad/porter)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)



## Abstract

Porter began in 2017 and provides data synchronization, but it's not just limited to data synchronization, it's widely used within the Suixingpay. Mainly provide the following features:

+ Database real-time synchronization
+ Database migrate
+ Database governance
+ Custom source and target data synchronization
+ Custom data extraction logic
 



## Core features

+ Native support Oracle|Mysql to Jdbc relational database final consistent synchronization
+ Plugin friendly, Support for custom source consumer plugins, target loading plugins, and alarm plugins for secondary development.
+ Support for custom source, target table, field mapping
+ Support configuration file based node synchronization task configuration.
+ Support management of background synchronization task push, node, task management. Provides task running indicator monitoring, node running logs, and task abnormal alarms.
+ Support node resource limit and allocation.
+ A distributed architecture based on the Zookeeper cluster plugin. Support for custom cluster plugins.

## Quick start

### Compile from source
```
git clone https://github.com/sxfad/porter.git
cd porter
git checkout version
gradle build
Find the installation package from the build/distributions list
```

### Configuration
[configuration document](https://github.com/sxfad/porter/blob/master/doc/profiles.md)

```
porter.id=unique id
#cluser
porter.cluster.strategy=ZOOKEEPER
porter.cluster.client.url=127.0.0.1:2181
porter.cluster.client.sessionTimeout=100000

#stastistics
porter.cluster.statistic.sourceType=KAFKA_PRODUCE
porter.cluster.statistic.servers=127.0.0.1:9200
porter.cluster.statistic.topic=your kafka topic

#standalone
porter.cluster.strategy=STANDALONE
porter.cluster.client.home=./.porter
```

### Run
```
tar zxvf build/distributions/porter-boot-version.tar
porter-boot-version/bin/porter-boot
```

### Debug
```
porter-boot-version/bin/porter-boot  debug port
```
### Operating environment
```
porter-boot-version/bin/porter-boot --spring.profiles.active=Operating environment
```
### Elegant close
```
porter-boot-version/bin/shutdown.sh
```

## Document
+ [tutoria @code-hipster](./doc/tutorial.md)
+ [Chinese document](./doc/document.md)
+ [English document](./doc/document_EN.md)

## Architecture
![architecture_design](doc/img/architecture.png)
![dataflow](doc/img/workflow.png)


## Screenshot

![Home](doc/img/Home.png)
+ [Manager Manual](./doc/manager_manual.md)


## Contact Us

* QQ group：835209101