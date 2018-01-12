# quick start
	
## 从源码编译
>
git clone http://192.168.120.68/root/suixingpay-datas.git <br/> 
cd suixingpay-datas <br/> 
git checkout 0.2 <br/> 
gradle build <br/> 
从build/distributions目录查找安装包

## 运行
>
tar zxvf build/distributions/node-boot-0.2.tar <br/>
node-boot-0.2/bin/startup.sh

### debug模式
>
node-boot-0.2/bin/startup.sh  debug 端口号

### 运行环境
>
node-boot-0.2/bin/startup.sh --spring.profiles.active=运行环境

## 优雅关闭
>
node-boot-0.2/bin/shutdown.sh




##  配置


[配置](./docs/profiles.md)


