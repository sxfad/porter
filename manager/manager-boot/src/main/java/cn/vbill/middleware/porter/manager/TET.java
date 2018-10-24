/**
 * 
 */
package cn.vbill.middleware.porter.manager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;

import com.alibaba.fastjson.JSON;

import cn.vbill.middleware.porter.common.config.TaskConfig;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class TET {
    public static void main(String[] args) {
        String oop = "porter.task[0].taskId=20180001\r\n" + 
                "\r\n" + 
                "porter.task[0].consumer.consumerName=CanalFetch\r\n" + 
                "porter.task[0].consumer.converter=canalRow\r\n" + 
                "porter.task[0].consumer.source.sourceType=CANAL\r\n" + 
                "porter.task[0].consumer.source.slaveId=1022\r\n" + 
                "porter.task[0].consumer.source.address=172.16.60.247:3306\r\n" + 
                "porter.task[0].consumer.source.database=ds_job\r\n" + 
                "porter.task[0].consumer.source.username=fd\r\n" + 
                "porter.task[0].consumer.source.password=123456\r\n" + 
                "porter.task[0].consumer.emptyFetchThreshold=-1\r\n" + 
                "\r\n" + 
                "porter.task[0].loader.loaderName=KAFKA_SYNC\r\n" + 
                "porter.task[0].loader.source.sourceType=KAFKA_PRODUCE\r\n" + 
                "porter.task[0].loader.source.servers=172.16.154.7:9092\r\n" + 
                "porter.task[0].loader.source.topic=KAFKA_LOADER_TEST\r\n" + 
                "";
        try {
            Properties pt = new Properties();
            pt.load(new ByteArrayInputStream(oop.getBytes()));
            System.out.println(pt.getProperty("porter.task[0].consumer.consumerName"));
            System.out.println(pt.getProperty("porter.task[0].loader.loaderName"));
            System.out.println(pt.getProperty("porter.task[0].consumer.emptyFetchThreshold"));
            System.out.println(pt.getProperty("porter.task[0].loader.source.servers"));
            //构造Teacher对象   
            TaskConfig taskConfig=new TaskConfig();   
              
            //赋值   
            try {
                BeanUtils.copyProperties(taskConfig,pt);
                System.out.println(JSON.toJSON(taskConfig));
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }   
          }
          catch (IOException e) {
            e.printStackTrace();
          }
    }
}
