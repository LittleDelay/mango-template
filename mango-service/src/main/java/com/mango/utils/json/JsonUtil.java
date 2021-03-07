package com.mango.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * JSON数据处理工具类
 *
 * @author Shangxp
 * @version 1.0.0
 * @since 2019/1/9 9:51
 */
@Slf4j
public class JsonUtil {

    /**
     * Jackson ObjectMapper
     */
    private static ObjectMapper objectMapper;

    /**
     * 获取Jackson ObjectMapper实例对象
     */
    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            try {
                objectMapper = new JacksonObjectMapperFactory().getObject();
            } catch (Exception e) {
                log.error("获取Jackson objectMapper实例对象发生异常", e);
            }
        }
        return objectMapper;
    }

    /**
     * 从请求流中解析JSON字符串并转换为JavaBean对象
     *
     * @param request HttpServletRequest请求对象
     * @param clazz   要转换的实体类型
     * @return JavaBean实体对象
     */
    public static <T> T toBean(HttpServletRequest request, Class<T> clazz) {
        try {
            ObjectMapper mapper = getObjectMapper();
            InputStream stream = request.getInputStream();
            return mapper.readValue(stream, clazz);
        } catch (Exception e) {
            log.error("从请求流中解析JSON字符串并转换为JavaBean对象发生异常", e);
        }
        return null;
    }

    /**
     * 从一个JSON对象字符格式中得到一个Java对象
     *
     * @param json  JSON字符串
     * @param clazz 要转换的实体类型
     * @return JavaBean实体对象
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            ObjectMapper mapper = getObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("JSON字符串转换Java对象发生异常", e);
        }
        return null;
    }

    /**
     * 将Java对象转换成JSON字符串
     *
     * @param obj JavaBean实体对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        try {
            ObjectMapper mapper = getObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Java对象转换JSON字符串发生异常", e);
        }
        return null;
    }

    /**
     * 将Java对象转换成JSON字符串, 忽略空值
     *
     * @param obj JavaBean实体对象
     * @return JSON字符串
     */
    public static String toJsonIgnoreNull(Object obj) {
        try {
            ObjectMapper mapper = getObjectMapper();
            // 忽略空值序列化
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Java对象转换JSON字符串发生异常", e);
        }
        return null;
    }

}