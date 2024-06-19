package com.lim.demos.notice.services.impls;

import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;
import com.lim.demos.notice.services.PhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PhoneServiceImpl
 * <p>手机服务的实现类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/19 上午10:48
 */
@Slf4j
@Service("phoneService")
public class PhoneServiceImpl implements PhoneService {

    /**
     * 方法：sendBirthdayNotice
     * <p>给receiver发送生日提醒的消息 </p>
     *
     * @param receiver 接收人信息
     * @param peoples 过生日的人员信息
     * @since 2024/6/19 上午9:36
     * @author lim
     */
    @Override
    public void sendBirthdayNotice(NoticeReceiver receiver, List<People> peoples) {

    }
}
