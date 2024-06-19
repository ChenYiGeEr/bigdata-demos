package com.lim.demos.notice.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lim.demos.notice.common.constants.Constants;
import com.lim.demos.notice.common.constants.NumberConstants;
import com.lim.demos.notice.common.converters.LunarSolarConverter;
import com.lim.demos.notice.common.enums.Gender;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.lim.demos.notice.common.utils.DateUtil.*;

/**
 * People
 * <p>人员信息</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 上午10:00
 */
public class People implements Serializable {

    /** 人员姓名 */
    private String name;

    /** 人员solar历出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dateOfSolarBirth;

    /** 人员lunar历出生日期 */
    private String dateOfLunarBirth;

    /** 生日类型 */
    private String birthdayTypes;

    /** 今年是否为闰月 */
    private Boolean leapMonth;

    /** 人员今年solar历生日时间 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date solarBirthday;

    /** lunar历生日 六月廿 */
    private String lunarBirthdayStr;

    /** 人员今年lunar历生日时间 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lunarBirthday;

    /** 性别 */
    private Gender gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfSolarBirth() {
        return dateOfSolarBirth;
    }

    public void setDateOfSolarBirth(Date dateOfSolarBirth) {
        this.dateOfSolarBirth = dateOfSolarBirth;
    }

    public String getDateOfLunarBirth() {
        return dateOfLunarBirth;
    }

    public void setDateOfLunarBirth(String dateOfLunarBirth) {
        this.dateOfLunarBirth = dateOfLunarBirth;
    }

    public String getBirthdayTypes() {
        return birthdayTypes;
    }

    public void setBirthdayTypes(String birthdayTypes) {
        this.birthdayTypes = birthdayTypes;
    }

    public Boolean getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(Boolean leapMonth) {
        leapMonth = leapMonth;
    }

    public Date getSolarBirthday() {
        // 获取今年太阳历生日
        if (Objects.nonNull(getDateOfSolarBirth())) {
            solarBirthday = getDateForThisYear(getDateMonth(getDateOfSolarBirth()), getDateDay(getDateOfSolarBirth()));
        }
        return solarBirthday;
    }

    public void setSolarBirthday(Date solarBirthday) {
        this.solarBirthday = solarBirthday;
    }

    public String getLunarBirthdayStr() {
        if (Objects.nonNull(getDateOfSolarBirth())) {
            lunarBirthdayStr = LunarSolarConverter.solarToLunar(getDateOfSolarBirth());
        }
        return lunarBirthdayStr;
    }

    public void setLunarBirthdayStr(String lunarBirthdayStr) {
        this.lunarBirthdayStr = lunarBirthdayStr;
    }

    public Date getLunarBirthday() {
        // 获取今年lunar历生日
        if (Objects.nonNull(getDateOfLunarBirth())) {
            String[] splits = getDateOfLunarBirth().split(String.format("\\%s", Constants.POINT));
            if (splits.length == NumberConstants.TWO_INT) {
                lunarBirthday = LunarSolarConverter.lunarToSolar(Integer.parseInt(splits[NumberConstants.ZERO_INT]), Integer.parseInt(splits[NumberConstants.ONE_INT]), getLeapMonth());
            }
        }
        return lunarBirthday;
    }

    public void setLunarBirthday(Date lunarBirthday) {
        this.lunarBirthday = lunarBirthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public People(String name, Date dateOfSolarBirth, String dateOfLunarBirth, String birthdayTypes, Boolean leapMonth, Date solarBirthday, String lunarBirthdayStr, Date lunarBirthday, Gender gender) {
        this.name = name;
        this.dateOfSolarBirth = dateOfSolarBirth;
        this.dateOfLunarBirth = dateOfLunarBirth;
        this.birthdayTypes = birthdayTypes;
        this.leapMonth = leapMonth;
        this.solarBirthday = solarBirthday;
        this.lunarBirthdayStr = lunarBirthdayStr;
        this.lunarBirthday = lunarBirthday;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("name", getName())
                .append("dateOfSolarBirth", getDateOfSolarBirth())
                .append("dateOfLunarBirth", getDateOfLunarBirth())
                .append("birthdayTypes", getBirthdayTypes())
                .append("leapMonth", getLeapMonth())
                .append("solarBirthday", getSolarBirthday())
                .append("lunarBirthdayStr", getLunarBirthdayStr())
                .append("lunarBirthday", getLunarBirthday())
                .append("gender", getGender())
                .toString();
    }
}
