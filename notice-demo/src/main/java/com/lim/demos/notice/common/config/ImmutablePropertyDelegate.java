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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * This class is used to get the properties from the classpath.
 * @author ds
 */
@Slf4j
public class ImmutablePropertyDelegate implements IPropertyDelegate {

    private static final String COMMON_PROPERTIES_NAME = "/common.properties";

    private final Properties properties;

    public ImmutablePropertyDelegate() {
        this(COMMON_PROPERTIES_NAME);
    }

    public ImmutablePropertyDelegate(String... propertyAbsolutePath) {
        properties = new Properties();
        // read from classpath
        for (String fileName : propertyAbsolutePath) {
            try (InputStream fis = getClass().getResourceAsStream(fileName)) {
                Properties subProperties = new Properties();
                subProperties.load(fis);
                properties.putAll(subProperties);
            } catch (IOException e) {
                log.error("Load property: {} error, please check if the file exist under classpath",
                        propertyAbsolutePath, e);
                System.exit(1);
            }
        }
        printProperties();
    }

    public ImmutablePropertyDelegate(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String get(String key) {
        return properties.getProperty(key);
    }

    @Override
    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    @Override
    public List<String> getStringList(String key, String separator) {
        String value = properties.getProperty(key);
        return StringUtils.isBlank(value) ? Collections.emptyList() : Arrays.asList(value.split(separator));
    }

    @Override
    public Set<String> getPropertyKeys() {
        return properties.stringPropertyNames();
    }

    private void printProperties() {
        properties.forEach((k, v) -> log.debug("Get property {} -> {}", k, v));
    }
}
