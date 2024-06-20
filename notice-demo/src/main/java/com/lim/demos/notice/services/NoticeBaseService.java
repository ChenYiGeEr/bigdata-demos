package com.lim.demos.notice.services;

import com.lim.demos.notice.common.utils.DateUtil;
import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    /**
     * 方法：getBirthdayMarkdownContentByPeoples
     * <p>获取生日提醒消息内容 </p>
     *
     * @param receiver 接收人
     * @param peoples 过生日的人员信息
     * @return java.lang.String 生日提醒邮件内容
     * @since 2024/6/19 上午9:32
     * @author lim
     */
    default String getBirthdayMarkdownContentByPeoples(NoticeReceiver receiver, List<People> peoples) {
        Date currentDate = new Date();
        StringBuilder peopleBirthdayMarkdown = new StringBuilder(StringUtils.EMPTY);
        peopleBirthdayMarkdown.append("### 【生日提醒】: 近期有人要过生日啦!\n")
                .append("亲爱的").append(receiver.getName()).append(receiver.getGender().getAppellation()).append(",\n")
                .append("这是一个温馨的生日提醒，表示近期有人要过生日啦。\n");
        People people;
        for (int i = 0; i < peoples.size(); i++) {
            people = peoples.get(i);
            peopleBirthdayMarkdown.append("- ")
                    .append(people.getName())
                    .append(people.getGender().getAppellation());
            // 农历生日
            if (Objects.nonNull(people.getLunarBirthday())) {
                peopleBirthdayMarkdown.append(" 农历生日是")
                        .append(DateUtil.CHINESE_DATE_FORMAT.format(people.getLunarBirthday()))
                        .append("，距今还有 **")
                        .append(DateUtil.calculateDaysDifference(currentDate, people.getLunarBirthday()))
                        .append("** 天");
            }
            // 公历生日
            if (Objects.nonNull(people.getSolarBirthday())) {
                peopleBirthdayMarkdown.append(" 公历生日是")
                        .append(DateUtil.CHINESE_DATE_FORMAT.format(people.getSolarBirthday()))
                        .append("，距今还有 **")
                        .append(DateUtil.calculateDaysDifference(currentDate, people.getSolarBirthday()))
                        .append("** 天");
            }
            if (i == peoples.size() - 1) {
                peopleBirthdayMarkdown.append("。\n\n");
            } else {
                peopleBirthdayMarkdown.append("；\n");
            }
        }
        peopleBirthdayMarkdown.append("让我们一起计划一些特别的活动，共同庆祝这些美好的日子。\n\n")
                .append("Best wishes, 您的生日提醒小助手\n\n")
                .append(DateUtil.CHINESE_DATE_FORMAT.format(currentDate));
        return peopleBirthdayMarkdown.toString();
    }

}
