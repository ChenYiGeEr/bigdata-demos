package com.lim.demos.notice.timers;

import com.lim.demos.notice.common.constants.Constants;
import com.lim.demos.notice.common.utils.DateUtil;
import com.lim.demos.notice.common.utils.JsonUtil;
import com.lim.demos.notice.common.utils.PropertyUtils;
import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;
import com.lim.demos.notice.services.EmailService;
import com.lim.demos.notice.services.ServerChanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * NoticeTimer
 * <p>通知定时器</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 上午10:23
 */
@Configuration(value = "noticeTimer")
public class NoticeTimer {

    private final Logger logger = LoggerFactory.getLogger(NoticeTimer.class);

    @Resource
    private EmailService emailService;

    @Resource
    private ServerChanService serverChanService;

    // @Scheduled(cron = "0/10 * * * * ?")
    /**
     * 方法：birthdayNotice
     * <p>每天早上七点半执行进行生日提醒（给消息接收人发送生日提醒）</p>
     *
     * @since 2024/6/18 上午10:25
     * @author lim
     */
    @Scheduled(cron = "0 30 7 * * ?")
    public void birthdayNotice() {
        // 0. 若用户在配置文件中关闭了生日提醒或者配置文件中无生日类型则不执行方法体
        if (!PropertyUtils.getBoolean(Constants.BIRTHDAY_NOTICE_ENABLE, Boolean.FALSE)) {
            return;
        }
        // 1. 格式化当前时间
        Date currentDate = new Date();
        String dateFormat = DateUtil.CHINESE_DATE_FORMAT.format(currentDate);
        logger.info("{}_NoticeTimer.birthdayNotice 生日提醒 start >>>", dateFormat);
        // 2. 工具类读取json文件内容并解析目标人员信息
        List<People> peoples = JsonUtil.parseArray(Constants.JSON_PEOPLES, People.class);
        // 3. 从peoples集合中过滤出近期(获取配置文件中的生日提醒倒计时)或当天生日的人员信息进行生日提醒
        Long birthdayCountdownDays = PropertyUtils.getLong(Constants.BIRTHDAY_COUNTDOWN_DAYS, 7L);
        List<People> birthTargetPeoples = peoples
                .stream()
                .filter(people ->
                    (people.getBirthdayTypes().contains(Constants.BIRTHDAY_TYPE_SOLAR) && Objects.nonNull(people.getSolarBirthday())
                            && DateUtil.calculateDaysDifference(currentDate, people.getSolarBirthday()) >= 0 && DateUtil.calculateDaysDifference(currentDate, people.getSolarBirthday()) <= birthdayCountdownDays)
                            || (people.getBirthdayTypes().contains(Constants.BIRTHDAY_TYPE_LUNAR) && Objects.nonNull(people.getLunarBirthday())
                            && DateUtil.calculateDaysDifference(currentDate, people.getLunarBirthday()) >= 0 && DateUtil.calculateDaysDifference(currentDate, people.getLunarBirthday()) <= birthdayCountdownDays)
                )
                .sorted(Comparator.comparing(People::getLunarBirthday))
                .collect(Collectors.toList());
        // 4. 对birthTargetPeoples集合进行非空判断，若集合为空则不进行生日提醒
        if (CollectionUtils.isEmpty(birthTargetPeoples)) {
            logger.warn("最近{}天没有人过生日......", birthdayCountdownDays);
            logger.info("{}_NoticeTimer.birthdayNotice 生日提醒 end >>>", dateFormat);
            return;
        }
        logger.info("最近{}天有人过生日......", birthdayCountdownDays);
        // 5. 获取提醒接收人集合信息，若没有接收人信息则不进行生日提醒
        List<NoticeReceiver> receivers = JsonUtil.parseArray(Constants.JSON_NOTICE_RECEIVERS, NoticeReceiver.class);
        if (CollectionUtils.isEmpty(receivers)) {
            logger.error("没有接收人信息......");
            logger.info("{}_NoticeTimer.birthdayNotice 生日提醒 end >>>", dateFormat);
            return;
        }

        // 6.1 TODO 手机短信提醒
        if (PropertyUtils.getBoolean(Constants.BIRTHDAY_NOTICE_PHONE_ENABLE, Boolean.FALSE)) {
            logger.info("手机提醒......");
        }

        // 6.2. DONE 邮件提醒
        if (PropertyUtils.getBoolean(Constants.BIRTHDAY_NOTICE_EMAIL_ENABLE, Boolean.FALSE)) {
            logger.info("邮箱提醒......");
            receivers.stream().filter(receiver -> !StringUtils.isEmpty(receiver.getEmailAddress())).forEach(receiver -> {
                emailService.sendBirthdayNotice(receiver, birthTargetPeoples);
            });
        }

        // 6.3. TODO 微信提醒
        if (PropertyUtils.getBoolean(Constants.BIRTHDAY_NOTICE_WECHAT_ENABLE, Boolean.FALSE)) {
            logger.info("微信提醒......");
        }

        // 6.4 server酱
        if (PropertyUtils.getBoolean(Constants.BIRTHDAY_NOTICE_SERVER_CHAN_ENABLE, Boolean.FALSE)) {
            logger.info("Server酱提醒......");
            receivers.stream().filter(receiver -> !StringUtils.isEmpty(receiver.getServerChanSendKey())).forEach(receiver -> {
                serverChanService.sendBirthdayNotice(receiver, birthTargetPeoples);
            });
        }

        logger.info("{}_NoticeTimer.birthdayNotice 生日提醒 end >>>", dateFormat);
    }

}
