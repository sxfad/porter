package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.DataSource;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据源信息表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataSourceMapper {

    /**
     * 新增
     *
     * @param dataSource
     */
    Integer insert(DataSource dataSource);

    /**
     * 修改
     *
     * @param dataSource
     */
    Integer update(@Param("id") Long id, @Param("dataSource") DataSource dataSource);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    DataSource selectById(Long id);

    /**
     * 分頁 条件查询:数据源名称 时间区间
     *
     * @return
     */
    List<DataSource> page(@Param("page") Page<DataSource> page, @Param("state") Integer state,
                          @Param("name") String name, @Param("beginTime") String beginTime,
                          @Param("endTime") String endTime, @Param("dataType") String dataType);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state, @Param("name") String name, @Param("beginTime") String beginTime,
                    @Param("endTime") String endTime, @Param("dataType") String dataType);

    /**
     * 校验新增
     *
     * @return
     */
    Integer insertSelective(DataSource dataSource);

    /**
     * 校验修改
     *
     * @param id
     * @param dataSource
     * @return
     */
    Integer updateSelective(@Param("id") Long id, @Param("dataSource") DataSource dataSource);

    /**
     * 消费数据来源分页All
     *
     * @param state
     * @return
     */
    Integer findByTypePageAll(@Param("state") Integer state);

    /**
     * 消费数据来源分页
     *
     * @param page
     * @param state
     * @return
     */
    List<DataSource> findByTypePage(@Param("page") Page<DataSource> page, @Param("state") Integer state);
}
