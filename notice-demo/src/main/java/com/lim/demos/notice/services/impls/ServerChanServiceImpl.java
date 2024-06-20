package com.lim.demos.notice.services.impls;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.lim.demos.notice.common.constants.BirthdayConstants;
import com.lim.demos.notice.common.constants.NumberConstants;
import com.lim.demos.notice.pojo.NoticeReceiver;
import com.lim.demos.notice.pojo.People;
import com.lim.demos.notice.pojo.message.ServerChanMessage;
import com.lim.demos.notice.services.ServerChanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            this.sendMarkdownMsg(
                    receiver.getServerChanSendKey(),
                    BirthdayConstants.EMAIL_NOTICE_SUBJECT,
                    getBirthdayMarkdownContentByPeoples(receiver, peoples)
            );
        } catch (Exception e) {
            log.error("sendBirthdayNotice ERROR");
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法：sendMarkdownMsg
     * <p>发送markdown消息 </p>
     *
     * @param serverChanSendKey 接收人key
     * @param title 标题
     * @param markdownContent markdown内容
     * @since 2024/6/19 上午9:41
     * @author lim
     */
    private void sendMarkdownMsg(String serverChanSendKey, String title, String markdownContent) {
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

}