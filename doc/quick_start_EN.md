# quick start
	
## Compile from source
```
git clone http://192.168.120.68/root/suixingpay-datas.git
cd suixingpay-datas
git checkout version
gradle build
Find the installation package from the build/distributions list
```

## Run
```
tar zxvf build/distributions/porter-boot-version.tar
porter-boot-version/bin/porter-boot
```

### Debug
- porter-boot-version/bin/porter-boot  debug port

### Operating environment
- porter-boot-version/bin/porter-boot --spring.profiles.active=Operating environment

## Elegant close
- porter-boot-version/bin/shutdown.sh

```
signalId = `kill -l USR2`
kill -$signalId PID  #centos:31 mac:12
```



