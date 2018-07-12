package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.AlarmUser;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 告警用户关联表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface AlarmUserMapper {

    /**
     * 新增
     *
     * @param alarmUser
     */
    Integer insert(AlarmUser alarmUser);

    /**
     * 修改
     *
     * @param alarmUser
     */
    Integer update(@Param("id") Long id, @Param("alarmUser") AlarmUser alarmUser);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer deleteByAlarmId(Long alarmId);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    AlarmUser selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<AlarmUser> page(@Param("page") Page<AlarmUser> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 根據alarmId查找數據
     *
     * @param alarmId
     * @return
     */
    List<AlarmUser> selectByAlarmId(Long alarmId);

}