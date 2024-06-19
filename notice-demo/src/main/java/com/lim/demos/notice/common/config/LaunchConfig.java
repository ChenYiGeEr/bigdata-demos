package com.lim.demos.notice.common.config;

import com.lim.demos.notice.common.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * LaunchConfig
 * <p>项目启动后执行</p>
 *
 * @author lim
 * @version 1.0
 * @since 2023/11/21 10:02
 */
@Configuration
public class LaunchConfig {

    /**
     * 获取日志对象，构造函数传入当前类，查找日志方便定位
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 端口
     */
    @Value("${server.port}")
    private String port;

    /**
     * context-path
     * */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 项目启动成功后打印部分启动信息
     */
    @Bean
    public ApplicationRunner applicationRunner() {
        return applicationArguments -> {
            try {
                InetAddress ia = InetAddress.getLocalHost();
                // 获取本机内网IP:port/<service>
                log.info("{}启动成功：http://{}:{}{}", Constants.SERVICE_NAME, ia.getHostAddress(), port, contextPath);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
        };
    }

}
