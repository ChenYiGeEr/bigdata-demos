/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lim.demos.notice.common.config;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * @author ds
 */
public interface IPropertyDelegate {

    /**
     * 方法：get
     * <p>读取配置文件common.properties，根据key获取字符串value </p>
     *
     * @param key key
     * @return java.lang.String
     * @since 2024/6/19 上午11:14
     * @author lim
     */
    String get(String key);

    /**
     * 方法：get
     * <p>读取配置文件common.properties，根据key获取字符串value，value不存在则返回默认值defaultValue </p>
     *
     * @param key key
     * @param defaultValue 默认值
     * @return java.lang.String
     * @since 2024/6/19 上午11:14
     * @author lim
     */
    String get(String key, String defaultValue);

    /**
     * 方法：get
     * <p>根据key获取的value，将value作为参数调用transformFunction方法 </p>
     *
     * @param key key
     * @param transformFunction 方法
     * @return T
     * @since 2024/6/19 上午11:25
     * @author lim
     */
    default <T> T get(String key, Function<String, T> transformFunction) {
        String value = get(key);
        if (value == null) {
            return null;
        }
        return transformFunction.apply(value);
    }

    /**
     * 方法：get
     * <p>根据key获取的value，若value不存在则defaultValue，将value或defaultValue作为参数调用transformFunction方法 </p>
     *
     * @param key key
     * @param transformFunction 方法
     * @param defaultValue 默认值
     * @return T
     * @since 2024/6/19 上午11:25
     * @author lim
     */
    default <T> T get(String key, Function<String, T> transformFunction, T defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return transformFunction.apply(value);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * 方法：getStringList
     * <p>读取配置文件common.properties，根据key获取字符串集合value </p>
     *
     * @param key key
     * @param separator 分隔符
     * @return java.lang.String
     * @since 2024/6/19 上午11:14
     * @author lim
     */
    List<String> getStringList(String key, String separator);

    /**
     * 方法：getPropertyKeys
     * <p>读取配置文件common.properties，获取key </p>
     *
     * @return java.util.Set
     * @since 2024/6/19 上午11:14
     * @author lim
     */
    Set<String> getPropertyKeys();

    /**
     * 方法：getOptional
     * <p>TODO </p>
     *
     * @param key key
     * @return java.util.Optional
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default Optional<String> getOptional(String key) {
        return getOptional(key, Function.identity());
    }

    /**
     * 方法：getOptional
     * <p>getOptional </p>
     *
     * @param key key
     * @param transformFunction 方法
     * @return java.util.Optional
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default <T> Optional<T> getOptional(String key, Function<String, T> transformFunction) {
        return Optional.ofNullable(get(key, transformFunction));
    }

    /**
     * 方法：getInt
     * <p>getInt </p>
     *
     * @param key key
     * @return java.lang.Integer
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default Integer getInt(String key) {
        return get(key, Integer::parseInt);
    }

    /**
     * 方法：getInt
     * <p>getInt </p>
     *
     * @param key key
     * @param defaultValue 默认值
     * @return java.lang.Integer
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default Integer getInt(String key, Integer defaultValue) {
        return get(key, Integer::parseInt, defaultValue);
    }

    /**
     * 方法：getLong
     * <p>根据key获取long类型的value </p>
     *
     * @param key key
     * @return java.lang.Long
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default Long getLong(String key) {
        return get(key, Long::parseLong);
    }

    /**
     * 方法：getLong
     * <p>根据key获取long类型的value，若value不存在则返回默认值 </p>
     *
     * @param key key
     * @param defaultValue 默认值
     * @return java.lang.Long
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default Long getLong(String key, Long defaultValue) {
        return get(key, Long::parseLong, defaultValue);
    }

    /**
     * 方法：getDouble
     * <p>根据key获取double类型的value </p>
     *
     * @param key key
     * @return java.lang.Double
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default Double getDouble(String key) {
        return get(key, Double::parseDouble);
    }

    /**
     * 方法：getDouble
     * <p>根据key获取double类型的value，若value不存在则返回默认值 </p>
     *
     * @param key key
     * @param defaultValue 默认值
     * @return java.lang.Double
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default Double getDouble(String key, Double defaultValue) {
        return get(key, Double::parseDouble, defaultValue);
    }

    /**
     * 方法：getBoolean
     * <p>根据key获取布尔类型的value </p>
     *
     * @param key key
     * @return java.lang.Boolean
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default Boolean getBoolean(String key) {
        return get(key, Boolean::parseBoolean);
    }

    /**
     * 方法：getBoolean
     * <p>根据key获取布尔类型的value，若value不存在则返回默认值 </p>
     *
     * @param key key
     * @param defaultValue 默认值
     * @return java.lang.Boolean
     * @since 2024/6/19 上午11:28
     * @author lim
     */
    default Boolean getBoolean(String key, Boolean defaultValue) {
        return get(key, Boolean::parseBoolean, defaultValue);
    }
}
