package com.lim.demos.notice.services.impls;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.lim.demos.notice.common.constants.BirthdayConstants;
import com.lim.demos.notice.common.constants.Constants;
import com.lim.demos.notice.common.constants.NumberConstants;
import com.lim.demos.notice.common.utils.PropertyUtils;
import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;
import com.lim.demos.notice.services.EnterpriseWechatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * EnterpriseWechatServiceImpl
 * <p>企业微信服务类的实现类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/21 上午9:20
 */
@Slf4j
@Service("enterpriseWechatService")
public class EnterpriseWechatServiceImpl implements EnterpriseWechatService {

    private static final String WEBHOOK = PropertyUtils.getString(Constants.BIRTHDAY_NOTICE_ENTERPRISE_WECHAT_WEBHOOK, StringUtils.EMPTY);

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
            this.sendEnterpriseWechatBotMsg(
                    BirthdayConstants.EMAIL_NOTICE_SUBJECT,
                    getBirthdayMarkdownContentByPeoples(receiver, peoples)
            );
        } catch (Exception e) {
            log.error("sendBirthdayNotice ERROR");
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法：sendEnterpriseWechatBotMsg
     * <p>发送企业微信机器人markdown消息 </p>
     *
     * @param subject 消息主题
     * @param msgContent 消息内容
     * @since 2024/6/21 上午9:28
     * @author lim
     */
    private void sendEnterpriseWechatBotMsg(String subject, String msgContent) {
        Map<String, Object> content = new HashMap<>(NumberConstants.TWO_INT);
        content.put("title", subject);
        content.put("content", msgContent);

        Map<String, Object> params = new HashMap<>(NumberConstants.FOUR_INT);
        params.put("msgtype", "markdown");
        params.put("markdown", content);

        HttpRequest request = HttpRequest
                .post(WEBHOOK)
                // 表单类型的Content-Type
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(JSON.toJSONString(params));
        HttpResponse response = request.execute();
        if (Objects.isNull(response) || response.getStatus() != NumberConstants.TWO_HUNDRED_INT) {
            throw new RuntimeException(response.body());
        }
    }

}