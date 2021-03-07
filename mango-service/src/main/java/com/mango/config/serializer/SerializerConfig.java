package com.mango.config.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 序列化/反序列化配置类
 *
 */
@Slf4j
@Configuration
@ControllerAdvice
public class SerializerConfig {

    /**
     * 在 Controller 做环切，参数进入 Handler 之前进行转换（针对 @RequestParam 或 @PathVariable）
     *
     * @param binder binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // String类型反序列化操作（去除字符串前后空格），空字符串转换为""
        StringTrimmerEditor propertyEditor = new StringTrimmerEditor(false);
        // 为 String 类对象注册编辑器
        binder.registerCustomEditor(String.class, propertyEditor);
    }

    /**
     * Json序列化和反序列化（针对@RequestBody）转换器
     *
     * @return Jackson2ObjectMapperBuilderCustomizer实例
     */
    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                // Long类型序列化，转换为字符串
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(Long.TYPE, ToStringSerializer.instance)
                // String类型反序列化操作（去除字符串前后空格），空字符串转换为""
                .deserializerByType(String.class, new StdScalarDeserializer<String>(String.class) {
                    private static final long serialVersionUID = 2405411545861105070L;

                    @Override
                    public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
                        return org.springframework.util.StringUtils.trimWhitespace(jsonParser.getValueAsString());
                    }
                })
                // LocalDate/LocalDateTime序列化
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializerByType(LocalDate.class,
                        new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .serializerByType(LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")))
                // LocalDate/LocalDateTime反序列化
                .featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .deserializerByType(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                        return SerializerConfig.this.parseLocalDateTime(jsonParser.getText().trim());
                    }
                })
                .deserializerByType(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                        return SerializerConfig.this.parseLocalDate(jsonParser.getText().trim());
                    }
                });
    }

    /**
     * 日期时间转换
     *
     * @return Converter实例
     */
    @Bean
    public Converter<String, LocalDateTime> convertLocalDateTime() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                try {
                    return SerializerConfig.this.parseLocalDateTime(source);
                } catch (Exception e) {
                    log.error("日期时间转换失败：{}", source, e);
                    throw e;
                }
            }
        };
    }

    /**
     * 日期转换
     *
     * @return Converter实例
     */
    @Bean
    public Converter<String, LocalDate> convertLocalDate() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                try {
                    return SerializerConfig.this.parseLocalDate(source);
                } catch (Exception e) {
                    log.error("日期时间转换失败：{}", source, e);
                    throw e;
                }
            }
        };
    }

    /**
     * 时间解析
     *
     * @param date 日期字符串
     * @return LocalDate实例
     */
    private LocalDate parseLocalDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        } catch (Exception e) {
            log.error("Could not parse date", e);
            throw new IllegalArgumentException("Could not parse date");
        }
    }

    /**
     * 日期时间解析
     *
     * @param dateTime 日期时间字符串
     * @return LocalDateTime实例
     */
    private LocalDateTime parseLocalDateTime(String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        try {
            String dateStr = dateTime.replace(" ", "T");
            String format = "uuuu-MM-dd'T'HH";
            switch (dateTime.length()) {
                case 10:
                    dateStr += "T00";
                    break;
                case 13:
                    break;
                case 16:
                    format += ":mm";
                    break;
                case 19:
                    format += ":mm:ss";
                    break;
                case 23:
                    format += ":mm:ss.SSS";
                    break;
                default:
                    throw new IllegalArgumentException("Could not parse date");
            }
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(format));
        } catch (IllegalArgumentException e) {
            log.error("Could not parse date", e);
            throw new IllegalArgumentException("Could not parse date");
        }
    }

}
