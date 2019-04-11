/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.common.warning.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 10:43
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 10:43
 */

public enum WarningErrorCode {
    HARDWARE_MEMORY(WarningCategory.PLATFORM, "内存不足或溢出", Pattern.compile(".*(OutOfMemoryError|GC overhead limit exceeded).*")),
    NETWORK_UNAVAILABLE(WarningCategory.TASK, "网络不可达", Pattern.compile(".*(Connection timed out|Timeout|timeout|CannotGetJdbcConnectionException).*")),
    TABLE_NOT_EXIST(WarningCategory.TASK, "表名不存在", Pattern.compile(".*(ORA-00942|table or view does not exist|查询不到目标仓库表结构).*")),
    COLUMN_NOT_EXIST(WarningCategory.TASK, "字段不存在", Pattern.compile(".*(ORA-01747|table.column|列说明无效|Unknown column).*")),
    DB_BUSY(WarningCategory.TASK, "数据库忙碌", Pattern.compile(".*(提交事务等待).*")),
    MYSQL_BINLOG_ERROR(WarningCategory.TASK, "读取binlog出错", Pattern.compile(".*(sqlstate = HY000 errmsg = log event entry exceeded max_allowed_packet).*")),
    MYSQL_BINLOG_NOT_EXIST(WarningCategory.TASK, "binlog不存在", Pattern.compile(".*(sqlstate = HY000 errmsg = Could not find first log file name in binary log index file).*")),
    TABLE_PARTITION_NOT_EXIST(WarningCategory.TASK, "表分区不存在", Pattern.compile(".*(Table has no partition|error code \\[1526\\]).*")),
    TABLE_NOT_MAPPING(WarningCategory.TASK, "表结构不一致", Pattern.compile(".*(表结构不一致).*")),
    VIOLATE_DATA_INTEGRITY(WarningCategory.TASK, "违反字段插入数据约束", Pattern.compile(".*(Data truncation|Out of range value for column"
            + "|DataIntegrityViolationException|ORA-12899|Value format invalid|value too large for column).*")),
    NO_PRIVILEGES(WarningCategory.TASK, "没有数据库权限", Pattern.compile(".*(SQLException with Error code '1031'|1031).*")),
    KAFKA_ESTABLISH_ERROR(WarningCategory.TASK, "kafka链接创建异常", Pattern.compile("(.*kafka.*(could not be established."
            + " Broker may not be available|Failed to construct kafka consumer|ConfigException).*)")),
    BAD_SQL_GRAMMAR(WarningCategory.TASK, "SQL语法错误", Pattern.compile(".*(BadSqlGrammarException|00926|SQLSyntaxErrorException).*")),
    UNRECOGNIZED_ERROR(WarningCategory.TASK, "不能识别的错误", null);
    private String desc;
    private Pattern character;
    private WarningCategory category;
    private static final List<WarningErrorCode> POOLS = Arrays.asList(HARDWARE_MEMORY, NETWORK_UNAVAILABLE, TABLE_NOT_EXIST, COLUMN_NOT_EXIST, DB_BUSY,
            MYSQL_BINLOG_ERROR, MYSQL_BINLOG_NOT_EXIST, TABLE_PARTITION_NOT_EXIST, TABLE_NOT_MAPPING, VIOLATE_DATA_INTEGRITY, BAD_SQL_GRAMMAR);
    WarningErrorCode(WarningCategory category, String desc, Pattern character) {
        this.desc = desc;
        this.character = character;
        this.category = category;
    }

    public static WarningErrorCode match(String msg) {
        if (!StringUtils.isBlank(msg)) {
            for (WarningErrorCode code : POOLS) {
                if (code.character.matcher(msg).matches()) {
                    return code;
                }
            }
        }
        return UNRECOGNIZED_ERROR;
    }

    public String getDesc() {
        return desc;
    }

    public WarningCategory getCategory() {
        return category;
    }
}
