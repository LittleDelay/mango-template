package com.mango.utils.xml;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.FactoryBean;

import java.text.SimpleDateFormat;

/**
 * Jackson XmlMapper对象工厂，已初始化默认配置
 *
 * @author Shangxp
 * @version 1.0.0
 * @since 2019/1/9 9:51
 */
public class JacksonXmlMapperFactory implements FactoryBean<XmlMapper> {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private String dateFormat = DEFAULT_DATE_PATTERN;

    @Override
    public XmlMapper getObject() {
        XmlMapper xmlMapper = new XmlMapper();
        // 设置可用单引号
        xmlMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        // 设置字段可以不用双引号包括
        xmlMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 设置时间格式
        xmlMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        // 禁用未知属性打断反序列化功能
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return xmlMapper;
    }

    @Override
    public Class<?> getObjectType() {
        return XmlMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}