/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2019-03-13 09:58:24  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.takin.mybatis.mapper.GenericMapper;
import com.suixingpay.takin.mybatis.service.AbstractService;
import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;
import cn.vbill.middleware.porter.manager.core.mapper.PublicDataSourceMapper;
import cn.vbill.middleware.porter.manager.service.PublicDataSourceService;

 /**  
 * 公共数据源配置表 服务实现类
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
@Service
public class PublicDataSourceServiceImpl extends AbstractService<PublicDataSource,Long> implements PublicDataSourceService {
	
	@Autowired
	private PublicDataSourceMapper publicDataSourceMapper;
	
	@Override
	protected GenericMapper<PublicDataSource, Long> getMapper() {
		return publicDataSourceMapper;
	}

	@Override
	public Integer insert(PublicDataSource publicDataSource) {
		return publicDataSourceMapper.insert(publicDataSource);
	}

	@Override
	public Integer update(Long id, PublicDataSource publicDataSource) {
        return publicDataSourceMapper.update(id, publicDataSource);
	}

	@Override
	public Integer delete(Long id) {
        return publicDataSourceMapper.delete(id);
	}

	@Override
	public PublicDataSource selectById(Long id) {
        return publicDataSourceMapper.selectById(id);
	}

	@Override
	public Page<PublicDataSource> page(Page<PublicDataSource> page) {
        Integer total = publicDataSourceMapper.pageAll(1);
        if(total>0) {
			page.setTotalItems(total);
			page.setResult(publicDataSourceMapper.page(page, 1));
        }
        return page;
	}
}
