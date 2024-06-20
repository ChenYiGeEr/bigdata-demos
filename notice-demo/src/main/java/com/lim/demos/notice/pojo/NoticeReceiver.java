package com.lim.demos.notice.pojo;

import com.lim.demos.notice.common.enums.Gender;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * NoticeReceiver
 * <p>提醒信息接收人</p>
 *
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 上午10:48
 */
public class NoticeReceiver implements Serializable {

    /** 人员姓名 */
    private String name;

    /** 人员性别 */
    private Gender gender;

    /** 人员手机号，多个手机号使用英文逗号隔开 */
    private String phoneNumber;

    /** 人员邮箱地址，多个邮箱地址使用英文逗号隔开 */
    private String emailAddress;

    /** 人员server酱sendKey，多个邮箱地址使用英文逗号隔开 */
    private String serverChanSendKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getServerChanSendKey() {
        return serverChanSendKey;
    }

    public void setServerChanSendKey(String serverChanSendKey) {
        this.serverChanSendKey = serverChanSendKey;
    }

    public NoticeReceiver(String name, String phoneNumber, String emailAddress, String serverChanSendKey) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.serverChanSendKey = serverChanSendKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("name", getName())
                .append("gender", getGender())
                .append("phoneNumber", getPhoneNumber())
                .append("emailAddress", getEmailAddress())
                .append("serverChanSendKey", getServerChanSendKey())
                .toString();
    }
}
