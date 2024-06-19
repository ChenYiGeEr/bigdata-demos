package com.lim.demos.notice.common.enums;

import lombok.Getter;

/**
 * Gender
 * <p>性别枚举</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 上午10:02
 */
@Getter
public enum Gender {

    /** 男性 */
    MALE("男", "先生"),

    /** 女性 */
    FEMALE("女", "女士"),

    /** 未知 */
    UNKNOWN("未知", "");

    /** 中文 */
    private final String chinese;

    /** 称谓 */
    private final String appellation;

    Gender(String chinese, String appellation) {
        this.chinese = chinese;
        this.appellation = appellation;
    }

}
