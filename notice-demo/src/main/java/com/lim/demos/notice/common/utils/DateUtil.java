package com.lim.demos.notice.common.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtil
 * <p> 日期工具类 </p>
 * @author lim
 * @version 1.0
 * @since 2024/6/18 上午9:32
 */
public class DateUtil {

    public static final SimpleDateFormat CHINESE_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 方法：calculateDaysDifference
     * <p>计算两个日期之间的天数差 </p>
     *
     * @param startDate 日期1
     * @param endDate 日期2
     * @return long 天数差
     * @since 2024/6/18 下午2:31
     * @author lim
     */
    public static long calculateDaysDifference(Date startDate, Date endDate) {
        return ChronoUnit.DAYS.between(convertToLocalDate(startDate), convertToLocalDate(endDate));
    }

    /**
     * 将 java.util.Date 转换为 java.time.LocalDate
     *
     * @param date 要转换的 Date 对象
     * @return 转换后的 LocalDate 对象
     */
    public static LocalDate convertToLocalDate(Date date) {
        // 确保日期对象不为空
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        // 将 Date 转换为 Instant
        Instant instant = date.toInstant();
        // 获取系统默认时区
        ZoneId zoneId = ZoneId.systemDefault();
        // 将 Instant 转换为 LocalDate
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 将 java.time.LocalDate 转换为 java.util.Date
     *
     * @param localDate 要转换的 localDate 对象
     * @return 转换后的 Date 对象
     */
    public static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取今年某月某日的 Date 对象
     *
     * @param month 月份（1-12）
     * @param day   日期（1-31）
     * @return 对应的 Date 对象
     */
    public static Date getDateForThisYear(int month, int day) {
        // 获取当前年份
        int year = LocalDate.now().getYear();
        // 创建对应的 LocalDate 对象
        LocalDate localDate = LocalDate.of(year, month, day);
        // 将 LocalDate 转换为 Date 对象
        return convertToDate(localDate);
    }

    /**
     * 方法：getDateMonth
     * <p>获取日期的月份 </p>
     *
     * @param date 日期
     * @return int 月份 1～12
     * @since 2024/6/18 下午2:53
     * @author lim
     */
    public static int getDateMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 月份从0开始，需要加1
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 方法：getDateDay
     * <p>获取日期的天数</p>
     *
     * @param date 日期
     * @return int 1～31
     * @since 2024/6/18 下午2:53
     * @author lim
     */
    public static int getDateDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

}