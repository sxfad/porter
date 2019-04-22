# <center>porter部署教程</center>

## 环境要求

- jdk
- zookeeper
- zkui(方便查看zookeeper集群的信息，没硬性要求可以不配，不影响同步功能)
- mysql
- node.js

## 环境配置过程

1. vmware安装的centos(其他Linux环境理论一样，未测试)

2. 安装jdk(我使用的是jdk1.8)

3. 安装zookeeper和zkui(可选)

4. 安装node.js(port-ui需要node环境)

5. 需要mysql服务器，porter-boot同步mysql数据采用的是canal组件(不需要安装canal，只需要在mysql中配置即可)

   a.canal的原理是基于mysql binlog技术，所以要开启mysql的binlog写入功能，建议配置binlog模式为row

   1. windows下mysql配置文件为my.ini，一般在mysql安装目录下
   2. Linux下mysql的配置文件为my.cnf,一般会放在/etc/my.cnf，/etc/mysql/my.cnf。如果找不到，可以用find命令查找。
   3. Linux用rpm包安装的mysql是不会安装/etc/my.cnf文件的


   ​	解决方案: 只需要复制一个/usr/share/mysql目录下的.cnf文件到/etc目录，并改名为my.cnf即可。

   ````
   [mysqld]
   log-bin=mysql-bin #添加这一行就行
   binlog-format=ROW #选择row模式
   server_id=1 #配置mysql replaction需要定义，不能和canal的slaveId重复
   ````

   b.canal的原理是模拟自己为mysql slave，所以这里一定需要做为mysql slave的相关权限

   ````sql
       CREATE USER canal IDENTIFIED BY 'canal';    
       GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';  
       -- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;  
       FLUSH PRIVILEGES; 
   ````

## 源码处理

笔者用到的是idea,需要安装git，gradle

1. porter项目源码加载到idea

   ````
   git init
   git clone https://github.com/sxfad/porter.git
   ````

2. 切换到3.0.2版本

3. 修改配置信息

   - 修改porter/manager/manager-boot/src/main/resources/application.properties 数据库连接信息

   ````properties
   #单机配置，集群只需要在url后边用逗号隔开填写多个ip即可
   manager.cluster.strategy=ZOOKEEPER
   manager.cluster.client.url=127.0.0.1:2181
   manager.cluster.client.sessionTimeout=100000
   
   #数据库连接信息,数据库需要与初始化脚本的数据库名相同
   spring.datasource.druid.url=jdbc:mysql://127.0.0.1:3306/ds_data?useUnicode=true&characterEncoding=utf-8
   spring.datasource.druid.username=test
   spring.datasource.druid.password=111111
   
   #端口
   #注意端口号要与porter-ui配置一致
   server.port=8081
   
   #log
   logging.file=${app.home}/logs/manager-boot.log
   ````

   - 初始化数据库:修改/porter/manager/manager-boot/src/main/resources/初始化脚本.sql

     1. 注意这里面的第一行"use ds_data",需要与jdbc连接信息一致
     2. 创建ds_data数据库，执行修改后的sql脚本

   - 修改/porter/porter/porter-boot/src/main/resources/application.properties配置⽂件

     ````properties
     logging.level.root=info
     logging.file=${app.home:./}/logs/data-node.log
     logging.level.com.alibaba.druid.pool.DruidDataSource=INFO
     server.port=8080
     
     #节点描述
     porter.id=1
     porter.gc=true
     #单机配置，集群只需要在url后边用逗号隔开填写多个ip即可
     porter.cluster.strategy=ZOOKEEPER
     porter.cluster.client.url=127.0.0.1:2181
     porter.cluster.client.sessionTimeout=100000
     
     #会加载tasks/sample文件夹下的任务配置
     spring.profiles.active=sample
     ````

   - 配置警告邮件信息，运行出错会发送邮件提醒(可选)

     修改⽂件/porter/porter/porter-boot/src/main/resources/application.properties

     ````properties
     node.statistic.upload=true
     ````

4. 构建项目

   - 点击idea右侧Gradle插件，选择porter(root)/Tasks/build/build

   - 待构建完成，拷⻉/porter/porter/porter-boot/build/distributions/porter-boot-3.0.tar 和/porter/manager/manager-boot/build/distributions/manager-boot-3.0.tar两个压缩包⾄/app/soft/porter/(看情况，⾃⼰指定)

     1. @Setter注解无法通过编译

        1. idea 勾选 Annotation Processors
        2. 安装 lombok
        3. 查看 java compile 是否为 javac

        按顺序执行前三步，如果还是build失败，则在项目根目录下的build.gradle文件中，subprojects下的dependencies中导入

        ````
        annotationProcessor 'org.projectlombok:lombok:1.16.20'
        ````

5. 配置管理UI

   - 下载源码  

     ````
     git init 
     git clone https://github.com/sxfad/porter-ui.git
     ````

   - 修改/porter-ui/local/local-ajax-base-url.js,注意此处端⼝为前⾯manager-boot指定的端口 export default 'http://IP:8081/api/manager';

   - 修改/porter-ui/ajax-config.js 'dev': 'http://IP:8081/api/manager'

   - 修改/porter-ui/builder/config.js(只做开发环境配置，这⾥的端⼝是⻚⾯访问端⼝)

     ````
     dev: { 
             env: '"development"', 
             port: 8082, 
             assetsSubDirectory: 'static', 
             assetsPublicPath: '/',
             cssSourceMap: false
         }
     ````

   - 复制修改后的porter-ui项⽬⾄服务器/app/soft/porter

6. 启动准备目前/app/soft/porter/⽬录下有三个项⽬：porter-ui,manager-boot,porter-boot

   - 启动zokeeper，启动zkui

   - 解压manager-boot-3.0.2.tar

   - 解压porter-boot-3.0.2.tar

   - 启动manager-boot

     ````
     ./app/soft/porter/manager-boot-3.0.2/bin/manager-boot
     ````


   - 启动前端(启动后不要ctrl+c退出命令⾏，后续操作请再创建窗⼝)

     ````
     cd /app/soft/porter/port-ui
     #安装环境
     yarn
     #启动
     yarn run dev
     ````

     1. 如果执行yarn命令提示yarn:command not found，则需要执行 npm install -g yarn

     2. 如果执行yarn成功，但是执行yarn run dev还是提示yarn:command not found，则需要执行

        ````
        export PATH=/usr/local/node-v10.15.0/bin/:$PATH
        echo $PATH
        ````

     现在浏览器输入IP:8082 admin/admin则可登录管理系统

   - 启动porter-boot

     ````
     ./app/soft/porter/porter-boot.3.0.2/bin/porter-boot
     ````

     1. 如果出现没有此文件或者目录则跟上述manager-boot解决方案相同

     2. 如果日志显示节点已经被注册则关闭porter-boot执行

        ````
        ./porter-boot-3.0.2/bin/porter-boot --force
        ````

7. 恭喜，部署成功

**注意： 项⽬关闭或启动出错时，请登录zkui，清除⼀下suixingpay节点的数据再启动**

**项⽬启动顺序建议：清理集群信息 -> 启动manager-boot -> 启动porter-ui -> 启动porter-boot**