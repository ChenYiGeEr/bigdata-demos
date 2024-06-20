package com.lim.demos.notice.services.impls;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.lim.demos.notice.common.constants.Constants;
import com.lim.demos.notice.common.constants.NumberConstants;
import com.lim.demos.notice.common.constants.WeChatNoticeConstants;
import com.lim.demos.notice.common.utils.PropertyUtils;
import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;
import com.lim.demos.notice.pojo.message.WechatAppMessage;
import com.lim.demos.notice.services.EnterpriseWechatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lim.demos.notice.common.constants.WeChatNoticeConstants.*;

/**
 * EnterpriseWechatServiceImpl
 * <p>企业微信服务类的实现类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/20 上午11:31
 */
@Slf4j
@Service("enterpriseWechatService")
public class EnterpriseWechatServiceImpl implements EnterpriseWechatService {

    private static final String TOKEN_REGEX = "{token}";

    private static final String CORP_ID_REGEX = "{corpId}";

    private static final String SECRET_REGEX = "{secret}";

    private static final String CORP_ID = PropertyUtils.getString(Constants.BIRTHDAY_NOTICE_WECHAT_ENTERPRISE_CORP_ID, Strings.EMPTY);

    private static final String CORP_SECRET = PropertyUtils.getString(Constants.BIRTHDAY_NOTICE_WECHAT_ENTERPRISE_CORP_SECRET, Strings.EMPTY);

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
            this.sendEnterpriseWeChatMsg(
                    receiver.getServerChanSendKey(),
                    getBirthdayMarkdownContentByPeoples(receiver, peoples)
            );
        } catch (Exception e) {
            log.error("sendBirthdayNotice ERROR");
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法：sendEnterpriseWeChatMsg
     * <p>企业微信发送markdown消息 </p>
     *
     * @param toWechatUId 接受消息的企业微信uid
     * @param markdownContent markdown内容
     * @since 2024/6/19 上午9:41
     * @author lim
     */
    private void sendEnterpriseWeChatMsg(String toWechatUId, String markdownContent) {
        // accessToken = 调用接口凭证
        String accessToken = getAccessToken();
        String weChatAgentIdChatId = null;
        // url替换
        String enterpriseWeChatPushUrlReplace = WeChatNoticeConstants.WE_CHAT_PUSH_URL.replace(TOKEN_REGEX, accessToken);
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put(WeChatNoticeConstants.WE_CHAT_CONTENT_KEY, markdownContent);
        WechatAppMessage wechatAppMessage = new WechatAppMessage(toWechatUId, "markdown",
                Integer.valueOf(weChatAgentIdChatId), contentMap, WE_CHAT_MESSAGE_SAFE_PUBLICITY,
                WE_CHAT_ENABLE_ID_TRANS, WE_CHAT_DUPLICATE_CHECK_INTERVAL_ZERO);
        // post请求url
        HttpRequest request = HttpRequest
                .post(enterpriseWeChatPushUrlReplace)
                // 表单类型的Content-Type
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(JSON.toJSONString(wechatAppMessage));
        HttpResponse response = request.execute();
        if (Objects.isNull(response) || response.getStatus() != NumberConstants.TWO_HUNDRED_INT) {
            throw new RuntimeException(response.toString());
        }
    }

    /**
     * 方法：getAccessToken
     * <p>根据corpId和corpSecret获取 accessToken </p>
     *
     * @return java.lang.String accessToken
     * @since 2024/6/20 下午2:54
     * @author lim
     */
    private String getAccessToken() {
        try {
            String url = WeChatNoticeConstants.WE_CHAT_TOKEN_URL
                    .replace(CORP_ID_REGEX, CORP_ID)
                    .replace(SECRET_REGEX, CORP_SECRET);
            HttpRequest.get(url).execute();
            return null;
        } catch (Exception e) {
            log.info("we chat alert get token error{}", e.getMessage());
            return null;
        }
    }

}
