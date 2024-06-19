package com.lim.demos.notice.common.utils;

import com.lim.demos.notice.common.config.IPropertyDelegate;
import com.lim.demos.notice.common.config.ImmutablePriorityPropertyDelegate;
import com.lim.demos.notice.common.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static com.lim.demos.notice.common.constants.Constants.COMMON_PROPERTIES_PATH;

/**
 * PropertyUtils
 * <p>配置工具类</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/18 上午10:34
 */
public class PropertyUtils {

    private static final IPropertyDelegate PROPERTY_DELEGATE =
            new ImmutablePriorityPropertyDelegate(COMMON_PROPERTIES_PATH);

    public static String getString(String key) {
        return PROPERTY_DELEGATE.get(key.trim());
    }

    public static String getString(String key, String defaultVal) {
        String val = getString(key);
        return StringUtils.isBlank(val) ? defaultVal : val;
    }

    public static List<String> getStringList(String key) {
        return PROPERTY_DELEGATE.getStringList(key.trim(), Constants.COMMA);
    }

    public static List<String> getStringList(String key, List<String> defaultVal) {
        List<String> list = getStringList(key);
        return CollectionUtils.isEmpty(list) ? defaultVal : list;
    }

    public static Long getLong(String key) {
        return PROPERTY_DELEGATE.getLong(key.trim());
    }

    public static Long getLong(String key, Long defaultVal) {
        Long val = PROPERTY_DELEGATE.getLong(key.trim());
        return Objects.isNull(val) ? defaultVal : val;
    }

    public static Boolean getBoolean(String key) {
        return PROPERTY_DELEGATE.getBoolean(key);
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return PROPERTY_DELEGATE.getBoolean(key, defaultValue);
    }

}
