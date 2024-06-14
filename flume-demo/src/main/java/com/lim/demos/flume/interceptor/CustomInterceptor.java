package com.lim.demos.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CustomInterceptor
 * <p>自定义拦截器</p>
 * @author lim
 * @version 1.0
 * @since 2023/8/3 22:02
 */
public class CustomInterceptor implements Interceptor {

    /**
     * 初始化方法，可以获取配置文件中的内容
     */
    @Override
    public void initialize() {}

    /**
     * 单个事件拦截
     * @param event
     * @return
     */
    @Override
    public Event intercept(Event event) {
        /* 判断若event为null则返回null */
        if (Objects.isNull(event)) { return null; }
        Map<String, String> headers = event.getHeaders();
        byte[] body = event.getBody();
        if (body[0] >= 'a' && body[0] <= 'z') {
            headers.put("type", "letter");
        } else if (body[0] >= '0' && body[0] <= '9') {
            headers.put("type", "number");
        } else {
            headers.put("type", "other");
        }
        return event;
    }

    /**
     * 批量事件拦截
     * @param list
     * @return
     */
    @Override
    public List<Event> intercept(List<Event> list) {
        list.forEach(this::intercept);
        list.removeIf(Objects::isNull);
        return list;
    }

    /**
     * 关闭资源
     */
    @Override
    public void close() {}



    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new CustomInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
