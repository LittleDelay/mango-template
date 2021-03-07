package com.mango;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动类
 *
 * @author Shangxp
 * @version 1.0.0
 * @since 2020/4/20 18:52
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.mango"})
@MapperScan(basePackages = {"com.mango.*.dao"})
@EnableFeignClients(basePackages = {
        "com.mango.*.*.service"
})
public class MangoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MangoServiceApplication.class, args);
    }

}
