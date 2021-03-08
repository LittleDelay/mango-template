package com.mango.utils.okhttp;

import okhttp3.MediaType;

/**
 * OkHttp常量数据定义
 *
 */
public class OkHttpConstants {

    /**
     * 请求超时时间
     */
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;
    public static final int CONNECT_TIMEOUT = 5;

    /**
     * JSON数据
     */
    protected static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * XML数据
     */
    protected static final MediaType MEDIA_TYPE_XML = MediaType.parse("application/xml; charset=utf-8");

    /**
     * 以二进制的形式上传文件
     */
    protected static final MediaType MEDIA_TYPE_OCTET_STREAM = MediaType.parse("application/octet-stream; charset=utf-8");

}
