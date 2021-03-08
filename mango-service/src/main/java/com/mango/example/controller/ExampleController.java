package com.mango.example.controller;


import com.mango.api.example.domain.request.AddExampleReq;
import com.mango.api.example.domain.request.ModifyExampleReq;
import com.mango.api.example.domain.request.QueryExampleListReq;
import com.mango.api.example.domain.request.QueryExamplePageReq;
import com.mango.api.example.domain.response.AddExampleRes;
import com.mango.api.example.domain.response.QueryExampleResponse;
import com.mango.api.example.service.ExampleService;
import com.mango.core.bean.response.ApiResponse;
import com.mango.core.bean.response.ResponseKit;
import com.mango.example.domain.model.Example;
import com.mango.example.domain.param.AddExampleParam;
import com.mango.example.domain.param.ModifyExampleParam;
import com.mango.example.domain.param.QueryExampleListParam;
import com.mango.example.domain.param.QueryExamplePageParam;
import com.mango.example.service.impl.ExampleServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 示例接口
 * <p>
 * GET-获取数据
 * POST-添加数据
 * PUT-修改数据
 * DELETE-删除数据
 *
 */
@RestController
@RequestMapping(ExampleService.BASE_URL)
public class ExampleController {

    @Autowired
    private ExampleServiceImpl exampleService;

    /**
     * 查询示例
     *
     * @param exampleId 示例ID
     * @return 示例
     */
    @GetMapping("/{exampleId}")
    public ApiResponse queryExample(@PathVariable("exampleId") Long exampleId) {
        final Example example = exampleService.getExampleById(exampleId);
        if (example == null) {
            return ResponseKit.fail(500, "示例不存在");
        }
        final QueryExampleResponse exampleResponse = new QueryExampleResponse().setExampleId(example.getId())
                .setExampleCode(example.getExampleCode())
                .setExampleName(example.getExampleName());
        return ResponseKit.success(exampleResponse);
    }

    /**
     * 查询示例列表
     *
     * @param queryExamplePageReq 请求参数
     * @return 响应结果
     */
    @GetMapping("/page")
    public ApiResponse queryExamplePage(
            @ModelAttribute QueryExamplePageReq queryExamplePageReq) {
        final QueryExamplePageParam queryExamplePageParam = new QueryExamplePageParam()
                .setExampleCode(queryExamplePageReq.getExampleCode())
                .setExampleName(queryExamplePageReq.getExampleName());
        queryExamplePageParam.setPageNum(queryExamplePageReq.getPageNum());
        queryExamplePageParam.setPageSize(queryExamplePageReq.getPageSize());

        return ResponseKit.success();
    }

    /**
     * 查询示例集合
     *
     * @param queryExampleListReq 请求参数
     * @return 响应结果
     */
    @GetMapping("/queryExampleList")
    public ApiResponse queryExampleList(
            @ModelAttribute QueryExampleListReq queryExampleListReq) {
        final QueryExampleListParam queryExampleListParam = new QueryExampleListParam()
                .setExampleCode(queryExampleListReq.getExampleCode())
                .setExampleName(queryExampleListReq.getExampleName());
        final List<Example> examples = exampleService.listExample(queryExampleListParam);

        return ResponseKit.success();
    }

    /**
     * 添加示例
     *
     * @param addExampleReq 请求参数
     * @return 响应结果
     */
    @PostMapping("/addExample")
    public ApiResponse addExample(@RequestBody AddExampleReq addExampleReq) {
        // 数据校验
        if (StringUtils.isBlank(addExampleReq.getExampleName())) {
            return ResponseKit.fail(500, "示例名称不能为空");
        }
        final AddExampleParam addExampleParam = new AddExampleParam()
                .setExampleName(addExampleReq.getExampleName());
        final Long exampleId = exampleService.addExample(addExampleParam);
        return ResponseKit.success(new AddExampleRes().setId(exampleId));
    }

    /**
     * 修改示例
     *
     * @param exampleId            示例ID
     * @param modifyExampleReq 请求参数
     * @return 响应结果
     */
    @PutMapping("/{exampleId}")
    public ApiResponse modifyExample(@PathVariable("exampleId") Long exampleId,
                                        @RequestBody ModifyExampleReq modifyExampleReq) {
        // 数据校验
        if (StringUtils.isBlank(modifyExampleReq.getExampleName())) {
            return ResponseKit.fail(500, "示例名称不能为空");
        }
        final Example example = exampleService.getExampleById(exampleId);
        if (example == null) {
            return ResponseKit.fail(500, "示例不存在");
        }

        final ModifyExampleParam modifyExampleParam = new ModifyExampleParam()
                .setExampleId(exampleId)
                .setExampleName(modifyExampleReq.getExampleName());
        exampleService.modifyExample(modifyExampleParam);
        return ResponseKit.success();
    }

}