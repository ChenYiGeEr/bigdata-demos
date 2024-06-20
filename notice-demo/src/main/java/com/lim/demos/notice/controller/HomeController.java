package com.lim.demos.notice.controller;

import com.lim.demos.notice.common.constants.Constants;
import com.lim.demos.notice.common.constants.NumberConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * HomeController
 *
 * @author lim
 * @version 1.0
 * @since 2023/11/21 10:09
 */
@RestController
@RequestMapping
public class HomeController {

    /**
     * 方法：home
     * <p>默认页 </p>
     *
     * @return java.util.Map
     * @since 2023/11/21 10:11
     * @author lim
     */
    @GetMapping
    public Map<String, Object> home() {
        return new HashMap<String, Object>(NumberConstants.TWO_INT) {{
                put("code", NumberConstants.TWO_HUNDRED_INT);
                put("msg", String.format("欢迎使用%s！", Constants.SERVICE_NAME));
            }};
    }

}
