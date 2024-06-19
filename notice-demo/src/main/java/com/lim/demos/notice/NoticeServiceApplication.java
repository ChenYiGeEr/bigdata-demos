package com.lim.demos.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * NoticeServiceApplication
 * <p>通知服务的入口类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 上午10:06
 */
@SpringBootApplication
@EnableScheduling
public class NoticeServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(NoticeServiceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类

        return builder.sources(NoticeServiceApplication.class);
    }

}
