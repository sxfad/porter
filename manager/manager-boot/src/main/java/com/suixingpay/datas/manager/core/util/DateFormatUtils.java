package com.suixingpay.datas.manager.core.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * 
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public abstract class DateFormatUtils {

    /**
     * 以T分隔日期和时间，并带时区信息，符合ISO8601规范
     */
    public static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    public static final String PATTERN_ISO_ON_SECOND = "yyyy-MM-dd'T'HH:mm:ssZZ";
    public static final String PATTERN_ISO_ON_DATE = "yyyy-MM-dd";

    /**
     * 以空格分隔日期和时间，不带时区信息
     */
    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_DEFAULT_ON_SECOND = "yyyy-MM-dd HH:mm:ss";

    // 使用工厂方法FastDateFormat.getInstance(), 从缓存中获取实例

    /**
     * 以T分隔日期和时间，并带时区信息，符合ISO8601规范
     */
    public static final FastDateFormat ISO_FORMAT = FastDateFormat.getInstance(PATTERN_ISO);
    public static final FastDateFormat ISO_ON_SECOND_FORMAT = FastDateFormat.getInstance(PATTERN_ISO_ON_SECOND);
    public static final FastDateFormat ISO_ON_DATE_FORMAT = FastDateFormat.getInstance(PATTERN_ISO_ON_DATE);

    /**
     * 以空格分隔日期和时间，不带时区信息
     */
    public static final FastDateFormat DEFAULT_FORMAT = FastDateFormat.getInstance(PATTERN_DEFAULT);
    public static final FastDateFormat DEFAULT_ON_SECOND_FORMAT = FastDateFormat.getInstance(PATTERN_DEFAULT_ON_SECOND);

    /**
     * 分析日期字符串, 仅用于pattern不固定的情况. 否则直接使用DateFormats中封装好的FastDateFormat.
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     */
    public static Date pareDate(String pattern, String dateString) throws ParseException {
        return FastDateFormat.getInstance(pattern).parse(dateString);
    }

    /**
     * 格式化日期, 仅用于pattern不固定的情况. 否则直接使用本类中封装好的FastDateFormat.
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     * 
     * @param pattern
     *            正则表达式
     * @param date
     *            日期
     * @return
     */
    public static String formatDate(String pattern, Date date) {
        return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 格式化日期, 仅用于不固定pattern不固定的情况. 否否则直接使用本类中封装好的FastDateFormat.
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     * 
     * @param pattern
     *            正则表达式
     * @param date
     *            日期
     * @return
     */
    public static String formatDate(String pattern, long date) {
        return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 按HH:mm:ss.SSS格式，格式化时间间隔. endDate必须大于startDate，间隔可大于1天
     * 
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static String formatDuration(Date startDate, Date endDate) {
        return DurationFormatUtils.formatDurationHMS(endDate.getTime() - startDate.getTime());
    }

    /**
     * 按HH:mm:ss.SSS格式，格式化时间间隔 单位为毫秒，必须大于0，可大于1天
     * 
     * @param durationMillis
     * @return
     */
    public static String formatDuration(long durationMillis) {
        return DurationFormatUtils.formatDurationHMS(durationMillis);
    }

    /**
     * 按HH:mm:ss格式，格式化时间间隔 endDate必须大于startDate，间隔可大于1天
     * 
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return String
     */
    public static String formatDurationOnSecond(Date startDate, Date endDate) {
        return DurationFormatUtils.formatDuration(endDate.getTime() - startDate.getTime(), "HH:mm:ss");
    }

    /**
     * 按HH:mm:ss格式，格式化时间间隔 单位为毫秒，必须大于0，可大于1天
     * 
     * @param durationMillis
     * @return
     */
    public static String formatDurationOnSecond(long durationMillis) {
        return DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss");
    }
}
