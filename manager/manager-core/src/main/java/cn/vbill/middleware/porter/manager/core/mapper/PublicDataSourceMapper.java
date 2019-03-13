/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2019-03-13 09:58:24  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.core.mapper;

import com.suixingpay.takin.mybatis.mapper.GenericMapper;
import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;

 /**  
 * 公共数据源配置表 Mapper接口
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
public interface PublicDataSourceMapper extends GenericMapper<PublicDataSource,Long> {

    /**
     * 新增(插入非空字段)
     * @param publicDataSource
     * @return Integer
     */
    Integer insert(PublicDataSource publicDataSource);

    /**
     * 新增(插入全部字段)
     * @param publicDataSource
     * @return Integer
     */
    Integer insertAllColumn(PublicDataSource publicDataSource);

    /**
     * 修改(修改非空字段)
     * @param publicDataSource
     * @return Integer
     */
    Integer update(@Param("id") Long id, @Param("publicDataSource") PublicDataSource publicDataSource);

    /**
     * 修改(修改全部字段)
     * @param loginUser
     * @return Integer
     */
    Integer updateAllColumn(@Param("id") Long id, @Param("publicDataSource") PublicDataSource publicDataSource);

    /**
     * 根据主键查找实体
     * @param id
     * @return LoginUser
     */
    PublicDataSource selectById(Long id);

    /**
     * list数据
     * @return List
     */
    List<PublicDataSource> selectList();

    /**
     * 分頁total
     * @param other
     * @return Integer
     */
    Integer pageAll(@Param("other") String other);

    /**
     * 分頁
     * @param page
     * @param other
     * @return List
     */
    List<PublicDataSource> page(@Param("page") Page<PublicDataSource> page, @Param("other") String other);

    /**
     * 刪除
     * @param id
     * @return Integer
     */
    Integer delete(Long id);

}