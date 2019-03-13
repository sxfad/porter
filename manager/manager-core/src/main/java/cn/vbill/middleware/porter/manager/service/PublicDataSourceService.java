/**
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.service;

import com.suixingpay.takin.mybatis.service.GenericService;
import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;

 /**
 * 公共数据源配置表 服务接口类
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
public interface PublicDataSourceService extends GenericService<PublicDataSource,Long> {

    Integer insert(PublicDataSource publicDataSource);

    Integer update(Long id, PublicDataSource publicDataSource);

    Integer delete(Long id);

    PublicDataSource selectById(Long id);

    Page<PublicDataSource> page(Page<PublicDataSource> page);

}
