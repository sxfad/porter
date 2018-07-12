/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 16:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.cluster.command;

import cn.vbill.middleware.porter.common.statistics.StatisticData;
import lombok.Getter;
import lombok.Setter;

/**
 * 统计信息到zk （服务器=》zk）
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 16:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 16:42
 */
public class StatisticUploadCommand implements ClusterCommand  {
    @Getter @Setter private StatisticData statisticData;

    public StatisticUploadCommand(StatisticData statisticData) {
        this.statisticData = statisticData;
    }
}
