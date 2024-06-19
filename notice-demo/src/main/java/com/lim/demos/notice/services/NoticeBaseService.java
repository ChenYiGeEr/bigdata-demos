package com.lim.demos.notice.services;

import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;

import java.util.List;

/**
 * NoticeBaseService
 * <p>通知基础服务</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/19 下午5:03
 */
public interface NoticeBaseService {

    /**
     * 方法：sendBirthdayNotice
     * <p>给receiver发送生日提醒的消息 </p>
     *
     * @param receiver 接收人信息
     * @param peoples 过生日的人员信息
     * @since 2024/6/19 上午9:36
     * @author lim
     */
    void sendBirthdayNotice(NoticeReceiver receiver, List<People> peoples);

}
