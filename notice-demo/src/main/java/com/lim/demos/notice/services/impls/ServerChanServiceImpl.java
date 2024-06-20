package com.lim.demos.notice.services.impls;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.lim.demos.notice.common.constants.BirthdayConstants;
import com.lim.demos.notice.common.constants.NumberConstants;
import com.lim.demos.notice.common.utils.DateUtil;
import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;
import com.lim.demos.notice.pojo.message.ServerChanMessage;
import com.lim.demos.notice.services.ServerChanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * ServerChanServiceImpl
 * <p></p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/19 下午4:56
 */
@Slf4j
@Service("serverChanService")
public class ServerChanServiceImpl implements ServerChanService {

    private static final String SERVER_CHAN_URL = "https://sctapi.ftqq.com/%s.send";

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
        try {
            this.sendMarkdownMessage(
                    receiver.getServerChanSendKey(),
                    BirthdayConstants.EMAIL_NOTICE_SUBJECT,
                    getBirthdayEmailMarkdownContentByPeoples(receiver, peoples)
            );
        } catch (Exception e) {
            log.error("sendBirthdayNoticeEmail ERROR");
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法：sendMimeEmail
     * <p>发送markdown消息 </p>
     *
     * @param serverChanSendKey 接收人key
     * @param title 标题
     * @param markdownContent markdown内容
     * @since 2024/6/19 上午9:41
     * @author lim
     */
    private void sendMarkdownMessage(String serverChanSendKey, String title, String markdownContent) {
        ServerChanMessage message = new ServerChanMessage() {{
                setTitle(title);
                setDesp(markdownContent);
                setNoip(NumberConstants.ONE_INT);
                setChannel(String.valueOf(NumberConstants.NINE_INT));
            }};
        HttpRequest request = HttpRequest
                .post(String.format(SERVER_CHAN_URL, serverChanSendKey))
                // 表单类型的Content-Type
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(JSON.toJSONString(message));
        HttpResponse response = request.execute();
        if (Objects.isNull(response) || response.getStatus() != NumberConstants.TWO_HUNDRED_INT) {
            throw new RuntimeException(response.toString());
        }
    }

    /**
     * 方法：getBirthdayEmailMarkdownContentByPeoples
     * <p>获取生日提醒消息内容 </p>
     *
     * @param receiver 邮件接收人
     * @param peoples 过生日的人员信息
     * @return java.lang.String 生日提醒邮件内容
     * @since 2024/6/19 上午9:32
     * @author lim
     */
    private String getBirthdayEmailMarkdownContentByPeoples(NoticeReceiver receiver, List<People> peoples) {
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