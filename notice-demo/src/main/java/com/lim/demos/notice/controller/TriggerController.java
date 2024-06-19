package com.lim.demos.notice.controller;

import com.lim.demos.notice.common.constants.NumberConstants;
import com.lim.demos.notice.timers.NoticeTimer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * TriggerController
 * <p>触发器controller</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/19 下午2:08
 */
@RestController
@RequestMapping("/trigger")
public class TriggerController {

    @Resource
    private NoticeTimer noticeTimer;

    /**
     * 方法：birthdayTrigger
     * <p>手动出发生日提醒的定时器 </p>
     *
     * @return java.util.Map
     * @since 2024/6/19 下午2:11
     * @author lim
     */
    @RequestMapping("/birthday")
    public Map<String, Object> birthdayTrigger() {
        noticeTimer.birthdayNotice();
        return new HashMap<String, Object>(NumberConstants.TWO_INT) {{
                put("code", NumberConstants.TWO_HUNDRED_INT);
                put("msg", "成功");
            }};
    }

}
