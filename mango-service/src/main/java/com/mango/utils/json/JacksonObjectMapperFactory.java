package com.mango.utils.json;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.FactoryBean;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Jackson ObjectMapper对象工厂，已初始化默认配置
 *
 */
public class JacksonObjectMapperFactory implements FactoryBean<ObjectMapper> {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private String dateFormat = DEFAULT_DATE_PATTERN;

    @Override
    public ObjectMapper getObject() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 设置可用单引号
        objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        // 设置字段可以不用双引号包括
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 禁用未知属性打断反序列化功能
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 设置时间格式
        objectMapper.setTimeZone(TimeZone.getDefault());
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper;
    }

    @Override
    public Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}