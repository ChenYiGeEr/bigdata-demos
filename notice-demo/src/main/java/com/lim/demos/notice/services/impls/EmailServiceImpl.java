package com.lim.demos.notice.services.impls;

import com.lim.demos.notice.common.constants.BirthdayConstants;
import com.lim.demos.notice.common.utils.DateUtil;
import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;
import com.lim.demos.notice.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * EmailServiceImpl
 * <p>邮件服务的实现类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 下午5:41
 */
@Slf4j
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @Resource
    private JavaMailSender emailSender;

    /**
     * 方法：sendBirthdayNoticeEmail
     * <p>给receiver发送生日提醒的短信 </p>
     *
     * @param receiver 接收人信息
     * @param peoples 过生日的人员信息
     * @since 2024/6/19 上午9:36
     * @author lim
     */
    @Override
    public void sendBirthdayNoticeEmail(NoticeReceiver receiver, List<People> peoples) {
        try {
            this.sendMimeEmail(
                    receiver.getEmailAddress(),
                    BirthdayConstants.EMAIL_NOTICE_SUBJECT,
                    getBirthdayEmailHtmlContentByPeoples(receiver, peoples)
            );
        } catch (MessagingException e) {
            log.error("sendBirthdayNoticeEmail ERROR");
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法：sendSimpleEmail
     * <p>发送简单的文本邮件(不支持附件、HTML 内容或嵌入资源)</p>
     *
     * @param toEmailAddress 接收人邮箱地址
     * @param subject 邮件主题
     * @param textContent 邮件文本内容
     * @since 2024/6/19 上午9:36
     * @author lim
     */
    private void sendSimpleEmail(String toEmailAddress, String subject, String textContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        // from = 发件人邮箱 to = 收件人邮箱 subject = 邮件主题 text = 邮件文本内容
        message.setFrom(senderEmailAddress);
        message.setTo(toEmailAddress);
        message.setSubject(subject);
        message.setText(textContent);
        emailSender.send(message);
    }

    /**
     * 方法：sendMimeEmail
     * <p>发送复杂的 MIME 邮件，可以包含 HTML 内容、附件和嵌入资源 </p>
     *
     * @param toEmailAddress 接收人邮箱地址
     * @param subject 邮件主题
     * @param htmlContent 邮件内容
     * @since 2024/6/19 上午9:41
     * @author lim
     */
    private void sendMimeEmail(String toEmailAddress, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setFrom(senderEmailAddress);
        messageHelper.setTo(toEmailAddress);
        messageHelper.setSubject(subject);
        messageHelper.setText(htmlContent, Boolean.TRUE);
        emailSender.send(message);
    }

    /**
     * 方法：getBirthdayEmailHtmlContentByPeoples
     * <p>获取生日提醒邮件内容 </p>
     *
     * @param receiver 邮件接收人
     * @param peoples 过生日的人员信息
     * @return java.lang.String 生日提醒邮件内容
     * @since 2024/6/19 上午9:32
     * @author lim
     */
    private String getBirthdayEmailHtmlContentByPeoples(NoticeReceiver receiver, List<People> peoples) {

        Date currentDate = new Date();
        StringBuilder peopleBirthdayHtml = new StringBuilder(StringUtils.EMPTY);

        People people;
        for (int i = 0; i < peoples.size(); i++) {
            people = peoples.get(i);
            // 张三先生 农历生日是1990年10月20日(腊月二十)，公历生日是1990年10月20日;
            peopleBirthdayHtml.append("<ul>● ")
                    .append(people.getName())
                    .append(people.getGender().getAppellation());
            // 农历生日
            if (Objects.nonNull(people.getLunarBirthday())) {
                peopleBirthdayHtml.append(" 农历生日是")
                        .append(DateUtil.CHINESE_DATE_FORMAT.format(people.getLunarBirthday()))
                        .append("，距今还有 <b style=\"\nfont-size: 18;\n\">")
                        .append(DateUtil.calculateDaysDifference(currentDate, people.getLunarBirthday()))
                        .append("</b> 天");
            }
            // 公历生日
            if (Objects.nonNull(people.getSolarBirthday())) {
                peopleBirthdayHtml.append(" 公历生日是")
                        .append(DateUtil.CHINESE_DATE_FORMAT.format(people.getSolarBirthday()))
                        .append("，距今还有 <b style=\"\nfont-size: 18;\n\">")
                        .append(DateUtil.calculateDaysDifference(currentDate, people.getSolarBirthday()))
                        .append("</b> 天");
            }
            if (i == peoples.size() - 1) {
                peopleBirthdayHtml.append("。");
            } else {
                peopleBirthdayHtml.append("；");
            }
            peopleBirthdayHtml.append("</ul>");
        }

        return "<!DOCTYPE html>"
                + "<html>"
                + "<body>"
                + "<h3>【生日提醒】: 近期有人要过生日啦!</h3>"
                + "<p>亲爱的" + receiver.getName() + receiver.getGender().getAppellation() + ",</p>"
                + "<p>这是一个温馨的生日提醒，表示近期有人要过生日啦。</p>"
                + peopleBirthdayHtml
                + "<p>让我们一起计划一些特别的活动，共同庆祝这些美好的日子。</p>"
                + "<p>Best wishes, 您的生日提醒小助手</p>"
                + "<p>" + DateUtil.CHINESE_DATE_FORMAT.format(currentDate) + "</p>"
                + "</body>"
                + "</html>";
    }

}
