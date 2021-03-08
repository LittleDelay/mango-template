package com.mango.utils.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Jackson XML数据处理工具类
 *
 */
@Slf4j
public class XmlUtil {

    /**
     * XML头信息
     */
    private static final String XML_DECLARE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

    /**
     * Jackson XmlMapper
     */
    private static XmlMapper xmlMapper;

    /**
     * 获取Jackson XmlMapper实例对象
     */
    private static XmlMapper getXmlMapper() {
        if (xmlMapper == null) {
            try {
                xmlMapper = new JacksonXmlMapperFactory().getObject();
            } catch (Exception e) {
                log.error("获取Jackson XmlMapper实例对象发生异常", e);
            }
        }
        return xmlMapper;
    }

    /**
     * 从请求流中解析XML字符串并转换为JavaBean对象
     *
     * @param request HttpServletRequest请求对象
     * @param clazz   要转换的实体类型
     * @return 对象
     */
    public static <T> T toBean(HttpServletRequest request, Class<T> clazz) {
        try {
            XmlMapper xmlMapper = getXmlMapper();
            InputStream stream = request.getInputStream();
            return xmlMapper.readValue(stream, clazz);
        } catch (IOException e) {
            log.error("从请求流中解析XML字符串并转换为JavaBean对象发生异常", e);
        }
        return null;
    }

    /**
     * 从一个XML对象字符格式中得到一个Java对象
     *
     * @param xml   XML字符串
     * @param clazz 要转换的实体类型
     * @return XML字符串转换后的实体对象
     */
    public static <T> T toBean(String xml, Class<T> clazz) {
        try {
            XmlMapper xmlMapper = getXmlMapper();
            return xmlMapper.readValue(xml, clazz);
        } catch (IOException e) {
            log.error("XML字符串转换Java对象发生异常", e);
        }
        return null;
    }

    /**
     * 将Java对象转换成XML字符串
     *
     * @param obj    要转换的实体对象
     * @param indent 是否对XML输出进行排版
     * @return 实体对象转换后的XML字符串
     */
    public static String toXml(Object obj, boolean indent) {
        return toXml(obj, indent, false);
    }

    /**
     * 将Java对象转换成XML字符串，忽略空值序列化
     *
     * @param obj    要转换的实体对象
     * @param indent 是否对XML输出进行排版
     * @return 实体对象转换后的XML字符串
     */
    public static String toXmlIgnoreNull(Object obj, boolean indent) {
        return toXml(obj, indent, true);
    }

    /**
     * 将Java对象转换成XML字符串
     *
     * @param obj        要转换的实体对象
     * @param indent     是否对XML输出进行排版
     * @param ignoreNull 是否忽略空值序列化
     * @return 实体对象转换后的XML字符串
     */
    private static String toXml(Object obj, boolean indent, boolean ignoreNull) {
        try {
            XmlMapper xmlMapper = getXmlMapper();
            // 忽略空值序列化
            if (ignoreNull) {
                xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }
            // 对XML输出进行排版
            if (indent) {
                xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            }
            // 构建XML内容
            StringBuilder builder = new StringBuilder();
            String xmlContent = xmlMapper.writeValueAsString(obj);
            builder.append(XML_DECLARE).append(xmlContent);
            return builder.toString();
        } catch (IOException e) {
            log.error("Java对象转换XML字符串发生异常", e);
        }
        return null;
    }

}