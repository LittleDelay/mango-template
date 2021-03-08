package com.mango.api.example.service;

import com.mango.api.common.constants.MangoConstants;
import com.mango.api.common.feign.MangoFeignConfig;
import com.mango.api.example.domain.request.QueryExampleListReq;
import com.mango.api.example.domain.request.QueryExamplePageReq;
import com.mango.core.bean.response.ResponseKit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 示例接口（提供需要对其他服务暴露的接口）
 */
@FeignClient(name = MangoConstants.SERVICE_NAME, decode404 = true, configuration = MangoFeignConfig.class)
public interface ExampleService {

    String BASE_URL = "/api/mango/examples";

    /**
     * 查询示例
     *
     * @param exampleId 示例ID
     * @return 示例
     */
    @GetMapping(BASE_URL + "/{exampleId}")
    ResponseKit getExample(@PathVariable("exampleId") Long exampleId);

    /**
     * 查询示例列表
     *
     * @param queryExamplePageReq 请求参数
     * @return 响应结果
     */
    @GetMapping(BASE_URL + "/page")
    ResponseKit queryExamplePage(
            @RequestParam QueryExamplePageReq queryExamplePageReq);

    /**
     * 查询示例集合
     *
     * @param queryExampleListReq 请求参数
     * @return 响应结果
     */
    @GetMapping(BASE_URL)
    ResponseKit queryExampleList(
            @RequestParam QueryExampleListReq queryExampleListReq);
}
