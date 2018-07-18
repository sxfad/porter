# quick start
	
## 从源码编译
```
git clone http://192.168.120.68/root/suixingpay-datas.git
cd suixingpay-datas
git checkout 版本
gradle build
从build/distributions目录查找安装包
```

## 运行
```
tar zxvf build/distributions/node-boot-版本.tar
node-boot-版本/bin/startup.sh
```

### 调试
- node-boot-版本/bin/startup.sh  debug 端口号

### 运行环境
- node-boot-版本/bin/startup.sh --spring.profiles.active=运行环境

## 优雅关闭
- node-boot-版本/bin/shutdown.sh

```
signalId = `kill -l USR2`
kill -$signalId PID  #centos:31 mac:12
```



