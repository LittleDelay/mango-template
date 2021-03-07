package com.mango.utils.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于OkHttp的网络请求工具类
 *
 * @author Shangxp
 * @version 1.0.0
 * @since 2020/4/20 16:00
 */
@Slf4j
public class OkHttpUtil {

    /**
     * OkHttpClient.Builder对象
     */
    private final OkHttpClient.Builder builder;

    /**
     * Builder参数构造方法
     *
     * @param builder OkHttpUtil.Builder实例
     */
    private OkHttpUtil(Builder builder) {
        this.builder = builder.builder;
    }

    /**
     * 获取自定义OkHttpClient实例
     */
    public OkHttpClient custom() {
        return this.builder.build();
    }

    /**
     * GET请求
     *
     * @param url 请求地址
     * @return 响应数据
     */
    public String get(String url) {
        return this.request(url, null);
    }

    /**
     * POST请求发送FORM表单数据
     *
     * @param url    请求地址
     * @param params 表单数据
     * @return 响应数据
     */
    public String postForm(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        // 添加参数
        if (!CollectionUtils.isEmpty(params)) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        return this.request(url, builder.build());
    }

    /**
     * POST请求发送JSON数据
     *
     * @param url  请求地址
     * @param json JSON数据
     * @return 响应数据
     */
    public String postJson(String url, String json) {
        if (StringUtils.isBlank(json)) {
            json = StringUtils.EMPTY;
        }
        RequestBody body = RequestBody.create(OkHttpConstants.MEDIA_TYPE_JSON, json);
        return this.request(url, body);
    }

    /**
     * POST请求发送XML数据
     *
     * @param url 请求地址
     * @param xml XML数据
     * @return 响应数据
     */
    public String postXml(String url, String xml) {
        if (StringUtils.isBlank(xml)) {
            xml = StringUtils.EMPTY;
        }
        RequestBody body = RequestBody.create(OkHttpConstants.MEDIA_TYPE_XML, xml);
        return this.request(url, body);
    }

    /**
     * 发送请求
     *
     * @param url         请求地址
     * @param requestBody 请求体
     * @return 响应数据
     */
    public String request(String url, RequestBody requestBody) {
        String data = StringUtils.EMPTY;

        // POST请求
        Request request;
        if (requestBody != null) {
            request = new Request.Builder().url(url).post(requestBody).build();
        }
        // GET请求
        else {
            request = new Request.Builder().url(url).build();
        }

        // 发送请求并处理响应
        try (Response response = this.custom().newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    data = responseBody.string();
                }
            }
        } catch (Exception ex) {
            log.error("OkHttp request error", ex);
        }

        return data;
    }

    /**
     * 异步POST请求发送JSON数据
     *
     * @param url      请求地址
     * @param json     JSON数据
     * @param listener 异步请求监听器
     */
    public void asyncPostJson(String url, final String json, final AsyncHttpListener listener) {
        // 设置请求实体对象
        RequestBody requestBody = RequestBody.create(OkHttpConstants.MEDIA_TYPE_JSON, json);
        // 构建OkHttp请求
        Request request = new Request.Builder().url(url).post(requestBody).build();
        // 发送异步请求并处理响应结果
        this.custom().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 获得返回体
                if (response.isSuccessful()) {
                    // 获取响应体
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        listener.onSuccess(responseBody.string());
                    } else {
                        listener.onFail(AsyncHttpListener.State.FAILURE, "请求失败，服务器返回数据错误");
                    }
                } else {
                    listener.onFail(AsyncHttpListener.State.FAILURE, "请求失败，服务器内部错误");
                }
            }

            @Override
            public void onFailure(Call call, IOException ex) {
                listener.onFail(AsyncHttpListener.State.NETWORK_ERROR, "请求失败");
                log.error("OKHttp async request error", ex);
            }
        });
    }

    /**
     * OkHttpUtil.Builder类定义
     */
    public static final class Builder {

        private final OkHttpClient.Builder builder;

        public Builder() {
            this.builder = new OkHttpClient.Builder();
        }

        /**
         * 创建OkHttpClient.Builder的默认实例
         */
        public Builder create() {
            // 设置超时时间
            this.builder.connectTimeout(OkHttpConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS);
            this.builder.readTimeout(OkHttpConstants.READ_TIMEOUT, TimeUnit.SECONDS);
            this.builder.writeTimeout(OkHttpConstants.WRITE_TIMEOUT, TimeUnit.SECONDS);
            // 错误重连
            this.builder.retryOnConnectionFailure(true);
            return this;
        }

        /**
         * 设置HTTPS连接参数
         */
        public Builder https() {
            // 获取证书信任管理器实例
            X509TrustManager trustManager = this.getDefaultTrustManager();
            // 注册SSLSocketFactory
            SSLSocketFactory sslSocketFactory = this.registerSslSocketFactory(trustManager);
            this.builder.sslSocketFactory(sslSocketFactory, trustManager);
            return this;
        }

        /**
         * 注册SSLSocketFactory
         *
         * @param trustManager 证书信任管理器
         * @return SSLSocketFactory实例对象
         */
        private SSLSocketFactory registerSslSocketFactory(X509TrustManager trustManager) {
            SSLSocketFactory sslSocketFactory = null;
            try {
                // 创建SSLContext对象，并使用我们指定的信任管理器初始化
                SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
                sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
                // 从上述SSLContext对象中得到SSLSocketFactory对象
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (Exception e) {
                log.error("注册SSLSocketFactory发生异常", e);
            }
            return sslSocketFactory;
        }

        /**
         * 获取证书信任管理器实例
         *
         * @return X509TrustManager实例对象
         */
        private X509TrustManager getDefaultTrustManager() {
            X509TrustManager trustManager = null;
            try {
                String algorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
                trustManagerFactory.init((KeyStore) null);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    String message = String.format("Unexpected default trust managers: %s", Arrays.toString(trustManagers));
                    throw new IllegalStateException(message);
                }
                trustManager = (X509TrustManager) trustManagers[0];
            } catch (Exception e) {
                log.error("获取证书信任管理器实例发生异常", e);
            }
            return trustManager;
        }

        /**
         * 构建OkHttpUtil实例
         *
         * @return OkHttpUtil实例对象
         */
        public OkHttpUtil build() {
            return new OkHttpUtil(this);
        }
    }

}