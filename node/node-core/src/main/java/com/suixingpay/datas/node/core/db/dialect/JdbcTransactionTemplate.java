/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月04日 18:06
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.dialect;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月04日 18:06
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月04日 18:06
 */
public class JdbcTransactionTemplate extends JdbcTemplate{
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTransactionTemplate.class);
    private TransactionTemplate transactionTemplate;

    public JdbcTransactionTemplate(DataSource dataSource) {
        super(dataSource);
    }

    protected void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }



    @Override
    public int update(String sql, Object... args) {
        int affect = 0;
        try {
            affect = transactionTemplate.execute(new TransactionCallback<Integer>() {
                @Override
                public Integer doInTransaction(TransactionStatus status) {
                    return updateWrap(sql, args);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.debug("sql:{},params:{},affect:{}", sql, JSON.toJSONString(Arrays.asList(args)), affect);
        return  affect;
    }



    public int update(String sql) {
        int affect = 0;
        try {
            affect = transactionTemplate.execute(new TransactionCallback<Integer>() {
                @Override
                public Integer doInTransaction(TransactionStatus status) {
                    return updateWrap(sql);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.debug("sql:{},affect:{}", sql, affect);
        return affect;
    }


    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        int[] affect = new int[]{};
        try {
            affect = transactionTemplate.execute(new TransactionCallback<int[]>() {
                @Override
                public int[] doInTransaction(TransactionStatus status) {
                    return batchUpdateWrap(sql, batchArgs);
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (null == affect || affect.length == 0) {
            List<Integer> affectList = new ArrayList<>();
            //分组执行
            batchErroUpdate(50, sql, batchArgs, 0, affectList);

            affect = Arrays.stream(affectList.toArray(new Integer[]{})).mapToInt(Integer::intValue).toArray();
        }
        LOGGER.debug("sql:{},params:{},affect:{}", sql, JSON.toJSONString(batchArgs), affect);
        return affect;
    }

    private void batchErroUpdate(int batchSize, String sql, List<Object[]> batchArgs,int from, List<Integer> affect) {
        int size = batchArgs.size();
        int batchEnd = from + batchSize;
        //获取当前分组
        List<Object[]> subArgs = new ArrayList<>();
        while (from < batchEnd && from < size) {
            subArgs.add(batchArgs.get(from));
            from ++;
        }

        //根据当前分组批量插入
        int[] reGroupAffect = null;
        try {
            reGroupAffect = transactionTemplate.execute(new TransactionCallback<int[]>() {
                @Override
                public int[] doInTransaction(TransactionStatus status) {
                    return batchUpdateWrap(sql, subArgs);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果仍然插入失败,改为单条插入
        if (null == reGroupAffect || reGroupAffect.length == 0) {
            for (int i = 0; i < subArgs.size(); i++) {
                affect.add(update(sql, subArgs.get(i)));
            }
        } else {
            Arrays.stream(reGroupAffect).boxed().forEach(i -> affect.add(i));
        }
        //递归下次分组
        if (batchEnd < size) batchErroUpdate(batchSize, sql, batchArgs, from, affect);
    }


    private int updateWrap(String sql) {
        return super.update(sql);
    }

    private int[] batchUpdateWrap(String sql, List<Object[]> batchArgs) {
        return super.batchUpdate(sql, batchArgs);
    }

    private int updateWrap(String sql, Object... args) {
        return super.update(sql, args);
    }
}
