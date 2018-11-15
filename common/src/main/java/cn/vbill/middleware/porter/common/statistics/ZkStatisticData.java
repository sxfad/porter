/**
 * 
 */
package cn.vbill.middleware.porter.common.statistics;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class ZkStatisticData extends StatisticData {

    @Override
    protected String getSubId() {
        return "KafkaListener";
    }

}
