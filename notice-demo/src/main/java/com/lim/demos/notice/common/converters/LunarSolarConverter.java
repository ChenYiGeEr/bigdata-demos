package com.lim.demos.notice.common.converters;

import cn.hutool.core.date.ChineseDate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * LunarSolarConverter
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 下午4:28
 */
public class LunarSolarConverter {
    /**
     * 将阳历日期转换为阴历日期
     *
     * @param date 阳历日期
     * @return 阴历日期字符串
     */
    public static String solarToLunar(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ChineseDate chineseDate = new ChineseDate(localDate);
        return chineseDate.toString();
    }

    /**
     * 将阴历日期转换为阳历日期
     *
     * @param month 阴历月份
     * @param day   阴历日期
     * @param leapMonth 是否为闰月
     * @return 阳历日期
     */
    public static Date lunarToSolar(int month, int day, boolean leapMonth) {
        // 获取当前年份
        int year = LocalDate.now().getYear();
        ChineseDate chineseDate = new ChineseDate(year, month, day, leapMonth);
        return chineseDate.getGregorianDate();
    }

}
