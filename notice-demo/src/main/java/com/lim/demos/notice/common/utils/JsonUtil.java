package com.lim.demos.notice.common.utils;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * JsonUtils
 * <p>json工具类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 上午10:51
 */
@Component
public class JsonUtil {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 方法：parseObject
     * <p>读取对应路径上的json文件内容并将其解析为json对象 </p>
     *
     * @param jsonAbsolutePath json文件绝对路径
     * @param clazz 类对象
     * @return T json对象
     * @since 2024/6/18 上午11:05
     * @author lim
     */
    public static <T> T parseObject(String jsonAbsolutePath, Class<T> clazz) {
        return JSON.parseObject(getJsonFileContent(jsonAbsolutePath), clazz);
    }

    /**
     * 方法：parseArray
     * <p>读取对应路径上的json文件内容并将其解析为json array </p>
     *
     * @param jsonAbsolutePath json文件绝对路径
     * @param clazz 类对象
     * @return java.util.List json array
     * @since 2024/6/18 上午11:06
     * @author lim
     */
    public static <T> List<T> parseArray(String jsonAbsolutePath, Class<T> clazz) {
        return JSON.parseArray(getJsonFileContent(jsonAbsolutePath), clazz);
    }

    /**
     * 方法：getJsonFileContent
     * <p>读取json文件内容 </p>
     *
     * @param jsonAbsolutePath json文件绝对路径
     * @return java.lang.String json内容
     * @since 2024/6/18 上午11:07
     * @author lim
     */
    private static String getJsonFileContent(String jsonAbsolutePath) {
        try {
            ClassPathResource resource = new ClassPathResource(jsonAbsolutePath);
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
