/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月01日 16:17
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.exception;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.suixingpay.datas.common.db.SqlErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.UncategorizedSQLException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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

    public static boolean isMatch(Throwable cause) {
        boolean match = false;

        if (cause instanceof DuplicateKeyException || cause instanceof SQLIntegrityConstraintViolationException) {
            return false;
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
                    || sqlError.getErrorCode() == SqlErrorCode.ERROR_1364.code;
                    //|| sqlError.getErrorCode() == SqlErrorCode.ERROR_1400.code;
        }
        return match;
    }
}
