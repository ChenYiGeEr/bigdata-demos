package com.lim.demos.notice.services.impls;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.lim.demos.notice.common.constants.BirthdayConstants;
import com.lim.demos.notice.common.constants.Constants;
import com.lim.demos.notice.common.constants.NumberConstants;
import com.lim.demos.notice.common.utils.DateUtil;
import com.lim.demos.notice.common.utils.PropertyUtils;
import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;
import com.lim.demos.notice.pojo.message.FeiShuMsgPostContentItem;
import com.lim.demos.notice.services.FeiShuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FeiShuServiceImpl
 * <p>飞书服务类的实现类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/20 下午3:06
 */
@Slf4j
@Service("feiShuService")
public class FeiShuServiceImpl implements FeiShuService {

    private static final String WEBHOOK = PropertyUtils.getString(Constants.BIRTHDAY_NOTICE_FEISHU_WEBHOOK, StringUtils.EMPTY);

    private static final String SECRET = PropertyUtils.getString(Constants.BIRTHDAY_NOTICE_FEISHU_SECRET, StringUtils.EMPTY);

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
            this.sendFeiShuMsg(
                    BirthdayConstants.EMAIL_NOTICE_SUBJECT,
                    getBirthdayRichTextByPeoples(peoples)
            );
        } catch (Exception e) {
            log.error("sendBirthdayNotice ERROR");
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法：sendFeiShuMsg
     * <p>发送飞书富文本消息 </p>
     *
     * @param subject 消息主题
     * @param msgContent 消息内容
     * @since 2024/6/19 上午9:41
     * @author lim
     */
    private void sendFeiShuMsg(String subject, List<FeiShuMsgPostContentItem> msgContent) {
        long timestamp = System.currentTimeMillis() / 1000L;
        Map<String, Object> params = new HashMap<>(NumberConstants.FOUR_INT);
        params.put("timestamp", timestamp);
        params.put("sign", genSignature(timestamp));
        params.put("msg_type", "post");
        Map<String, Object> content = new HashMap<>(NumberConstants.ONE_INT);
        Map<String, Map<String, Object>> post = new LinkedHashMap() {{
                put("zh_cn", new HashMap<String, Object>() {{
                        put("title", subject);
                        put("content", Stream.of(msgContent).collect(Collectors.toList()));
                    }});
            }};
        content.put("post", post);
        params.put("content", content);
        HttpRequest request = HttpRequest
                .post(WEBHOOK)
                // 表单类型的Content-Type
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(JSON.toJSONString(params));
        HttpResponse response = request.execute();
        if (Objects.isNull(response) || response.getStatus() != NumberConstants.TWO_HUNDRED_INT) {
            throw new RuntimeException(response.toString());
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
        //使用HmacSHA256算法计算签名
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] signData = mac.doFinal(new byte[]{});
        return new String(Base64.encodeBase64(signData));
    }

    /**
     * 方法：getBirthdayRichTextByPeoples
     * <p>获取富文本 </p>
     *
     * @param peoples 过生日的人员信息
     * @return java.util.List
     * @since 2024/6/20 下午4:29
     * @author lim
     */
    private List<FeiShuMsgPostContentItem> getBirthdayRichTextByPeoples(List<People> peoples) {
        Date currentDate = new Date();
        List<FeiShuMsgPostContentItem> contents = new ArrayList<>(peoples.size() + NumberConstants.THREE_INT);
        contents.add(new FeiShuMsgPostContentItem("亲爱的，这是一个温馨的生日提醒，表示近期有人要过生日啦。\n"));
        People people;
        for (int i = 0; i < peoples.size(); i++) {
            people = peoples.get(i);
            StringBuilder peopleBirthdayMarkdown = new StringBuilder()
                    .append("• ")
                    .append(people.getName())
                    .append(people.getGender().getAppellation());
            // 农历生日
            if (Objects.nonNull(people.getLunarBirthday())) {
                peopleBirthdayMarkdown.append(" 农历生日是")
                        .append(DateUtil.CHINESE_DATE_FORMAT.format(people.getLunarBirthday()))
                        .append("，距今还有 ")
                        .append(DateUtil.calculateDaysDifference(currentDate, people.getLunarBirthday()))
                        .append(" 天");
            }
            // 公历生日
            if (Objects.nonNull(people.getSolarBirthday())) {
                peopleBirthdayMarkdown.append(" 公历生日是")
                        .append(DateUtil.CHINESE_DATE_FORMAT.format(people.getSolarBirthday()))
                        .append("，距今还有 ")
                        .append(DateUtil.calculateDaysDifference(currentDate, people.getSolarBirthday()))
                        .append(" 天");
            }
            if (i == peoples.size() - 1) {
                peopleBirthdayMarkdown.append("。\n");
            } else {
                peopleBirthdayMarkdown.append("；\n");
            }
            contents.add(new FeiShuMsgPostContentItem(peopleBirthdayMarkdown.toString()));
        }
        contents.add(new FeiShuMsgPostContentItem("让我们一起计划一些特别的活动，共同庆祝这些美好的日子。\n"));
        contents.add(new FeiShuMsgPostContentItem("Best wishes, 您的生日提醒小助手\n"));
        contents.add(new FeiShuMsgPostContentItem(DateUtil.CHINESE_DATE_FORMAT.format(currentDate)));
        return contents;
    }

}