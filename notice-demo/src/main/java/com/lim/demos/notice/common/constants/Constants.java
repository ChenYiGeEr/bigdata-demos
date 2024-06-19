package com.lim.demos.notice.common.constants;

/**
 * Constants
 * <p>常量类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 上午10:32
 */
public class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Construct Constants");
    }

    /**
     * 个性化提醒服务服务名称
     */
    public static final String SERVICE_NAME = "个性化提醒服务[notice-service]";

    /**
     * common properties path
     */
    public static final String COMMON_PROPERTIES_PATH = "/common.properties";

    /**
     * json peoples.json
     */
    public static final String JSON_PEOPLES = "jsons/peoples.json";

    /**
     * json notice_receiver.json
     */
    public static final String JSON_NOTICE_RECEIVERS = "jsons/notice_receiver.json";

    public static final String BIRTHDAY_TYPE_SOLAR = "solar";

    public static final String BIRTHDAY_TYPE_LUNAR = "lunar";

    /**
     * birthday.notice.enable
     */
    public static final String BIRTHDAY_NOTICE_ENABLE = "birthday.notice.enable";

    /**
     * birthday.notice.phone.enable
     */
    public static final String BIRTHDAY_NOTICE_PHONE_ENABLE = "birthday.notice.phone.enable";

    /**
     * birthday.notice.email.enable
     */
    public static final String BIRTHDAY_NOTICE_EMAIL_ENABLE = "birthday.notice.email.enable";

    /**
     * birthday.notice.wechat.enable
     */
    public static final String BIRTHDAY_NOTICE_WECHAT_ENABLE = "birthday.notice.wechat.enable";

    /**
     * birthday.notice.serverchan.enable
     */
    public static final String BIRTHDAY_NOTICE_SERVER_CHAN_ENABLE = "birthday.notice.serverchan.enable";

    /**
     * birthday.countdown
     */
    public static final String BIRTHDAY_COUNTDOWN_DAYS  = "birthday.countdown";

    /**
     * comma ,
     */
    public static final String COMMA = ",";

    /**
     * POINT .
     */
    public static final String POINT = ".";

}
