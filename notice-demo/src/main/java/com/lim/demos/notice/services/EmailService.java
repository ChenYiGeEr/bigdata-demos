package com.lim.demos.notice.services;

import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;

import java.util.List;

/**
 * EmailService
 * <p>邮箱服务</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 下午5:39
 */
public interface EmailService {

    /**
     * 方法：sendBirthdayNoticeEmail
     * <p>给receiver发送生日提醒的短信 </p>
     *
     * @param receiver 接收人信息
     * @param peoples 过生日的人员信息
     * @since 2024/6/19 上午9:36
     * @author lim
     */
    void sendBirthdayNoticeEmail(NoticeReceiver receiver, List<People> peoples);

}
