# quick start
	
## 从源码编译
```
git clone http://192.168.120.68/root/vbill-proter.git
cd vbill-proter
git checkout 版本
gradle build
从build/distributions目录查找安装包
```

## 运行
```
tar zxvf build/distributions/porter-boot-版本.tar
porter-boot-版本/bin/porter-boot
```

### 调试
- porter-boot-版本/bin/porter-boot  debug 端口号

### 运行环境
- porter-boot-版本/bin/porter-boot --spring.profiles.active=运行环境

## 优雅关闭
- porter-boot-版本/bin/shutdown.sh

```
signalId = `kill -l USR2`
kill -$signalId PID  #centos:31 mac:12
```



