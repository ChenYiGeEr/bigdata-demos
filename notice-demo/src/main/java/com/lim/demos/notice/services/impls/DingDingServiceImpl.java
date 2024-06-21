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
import com.lim.demos.notice.services.DingDingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * DingDingServiceImpl
 * <p>钉钉服务类的实现类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/20 下午5:38
 */
@Slf4j
@Service("dingDingService")
public class DingDingServiceImpl implements DingDingService {

    private static final String WEBHOOK = PropertyUtils.getString(Constants.BIRTHDAY_NOTICE_DINGDING_WEBHOOK, StringUtils.EMPTY);

    private static final String SECRET = PropertyUtils.getString(Constants.BIRTHDAY_NOTICE_DINGDING_SECRET, StringUtils.EMPTY);

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
            this.sendDingDingBotMsg(
                    BirthdayConstants.EMAIL_NOTICE_SUBJECT,
                    getBirthdayMarkdownContentByPeoples(receiver, peoples)
            );
        } catch (Exception e) {
            log.error("sendBirthdayNotice ERROR");
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法：sendDingDingBotMsg
     * <p>发送钉钉机器人markdown消息 </p>
     *
     * @param subject 消息主题
     * @param msgContent 消息内容
     * @since 2024/6/19 上午9:41
     * @author lim
     */
    private void sendDingDingBotMsg(String subject, String msgContent) {
        Map<String, Object> content = new HashMap<>(NumberConstants.TWO_INT);
        content.put("title", subject);
        content.put("text", msgContent);

        long timestamp = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<>(NumberConstants.FOUR_INT);
        params.put("msgtype", "markdown");
        params.put("markdown", content);
        HttpRequest request = HttpRequest
                .post(WEBHOOK + "&timestamp=" + timestamp + "&sign=" + genSignature(timestamp))
                // 表单类型的Content-Type
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(JSON.toJSONString(params));
        HttpResponse response = request.execute();
        if (Objects.isNull(response) || response.getStatus() != NumberConstants.TWO_HUNDRED_INT) {
            throw new RuntimeException(response.body());
        }
    }

    /**
     * 方法：genSignature
     * <p>根据时间戳和SECRET获取签名 </p>
     *
     * @param timestamp 秒级时间戳
     * @return java.lang.String 签名
     * @since 2024/6/20 下午4:14
     * @author lim
     */
    private String genSignature(long timestamp) {
        if (StringUtils.isBlank(SECRET)) {
            return SECRET;
        }
        String stringToSign = timestamp + "\n" + SECRET;
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String signature = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            return signature;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}