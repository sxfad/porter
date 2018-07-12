/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月01日 16:17
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.exception;

import com.alibaba.druid.pool.DataSourceClosedException;
import com.alibaba.druid.pool.DataSourceDisableException;
import com.alibaba.druid.pool.DataSourceNotAvailableException;
import com.alibaba.druid.pool.GetConnectionTimeoutException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import cn.vbill.middleware.porter.common.db.SqlErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.UncategorizedSQLException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLRecoverableException;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月01日 16:17
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月01日 16:17
 */
public class TaskStopTriggerException extends TaskException {

    public TaskStopTriggerException(String message) {
        super(message);
    }

    public TaskStopTriggerException(Throwable cause) {
        super(cause);
    }

    public static boolean isMatch(Throwable cause, String sqlType) {
        boolean match = false;

        if (cause instanceof DuplicateKeyException || cause instanceof SQLIntegrityConstraintViolationException) {
            return false;
        }

        /**
         * druid 数据库连接异常
         * 在遇到数据库重启等问题导致任务无法继续同步时触发报警机制
         */
        if (cause instanceof DataSourceClosedException || cause instanceof DataSourceDisableException
                || cause instanceof DataSourceNotAvailableException || cause instanceof GetConnectionTimeoutException
                || cause instanceof SQLRecoverableException) {
            return true;
        }
        if (cause instanceof CannotGetJdbcConnectionException || cause instanceof UncategorizedSQLException
                || cause instanceof MySQLSyntaxErrorException || cause instanceof BadSqlGrammarException
                || cause instanceof DataIntegrityViolationException) {
            return true;
        }

        if (cause instanceof SQLException) {
            SQLException sqlError = (SQLException) cause;
            return sqlError.getErrorCode() == SqlErrorCode.ERROR_904.code || sqlError.getErrorCode() == SqlErrorCode.ERROR_942.code
                    || sqlError.getErrorCode() == SqlErrorCode.ERROR_1438.code || sqlError.getErrorCode() == SqlErrorCode.ERROR_12899.code
                    || sqlError.getErrorCode() == SqlErrorCode.ERROR_1364.code
                    || sqlError.getErrorCode() == SqlErrorCode.ERROR_17002.code
                    || (StringUtils.isNotBlank(sqlType) && StringUtils.trimToEmpty(sqlType).equalsIgnoreCase("INSERT")
                    && sqlError.getErrorCode() == SqlErrorCode.ERROR_1400.code);
        }
        return match;
    }
    public static boolean isMatch(Throwable cause) {
        return isMatch(cause, null);
    }
}
