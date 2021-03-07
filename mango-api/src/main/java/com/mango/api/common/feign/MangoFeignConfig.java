package com.mango.api.common.feign;

import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign配置
 */
@Configuration
public class MangoFeignConfig {

    /**
     * feign client 失败不重试
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1, 0);
    }

    /**
     * feign client 超时时间设置
     */
    @Bean
    Request.Options feignOptions() {
        return new Request.Options(10000, 6000 * 1000);
    }

}
