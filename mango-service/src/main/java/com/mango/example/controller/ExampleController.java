package com.mango.example.controller;

import com.bzn.backend.api.example.domain.request.AddExampleRequest;
import com.bzn.backend.api.example.domain.request.ModifyExampleRequest;
import com.bzn.backend.api.example.domain.request.QueryExampleListRequest;
import com.bzn.backend.api.example.domain.request.QueryExamplePageRequest;
import com.bzn.backend.api.example.domain.response.AddExampleResponse;
import com.bzn.backend.api.example.domain.response.QueryExampleListResponse;
import com.bzn.backend.api.example.domain.response.QueryExamplePageResponse;
import com.bzn.backend.api.example.domain.response.QueryExampleResponse;
import com.bzn.backend.api.example.service.ExampleService;
import com.mango.example.domain.bo.ExamplePageBo;
import com.mango.example.domain.model.Example;
import com.mango.example.domain.param.AddExampleParam;
import com.mango.example.domain.param.ModifyExampleParam;
import com.mango.example.domain.param.QueryExampleListParam;
import com.mango.example.domain.param.QueryExamplePageParam;
import com.mango.example.service.impl.ExampleServiceImpl;
import com.bzn.util.page.PageListResponse;
import com.bzn.util.response.ResponseBzn;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseBzn<QueryExampleResponse> queryExample(@PathVariable("exampleId") Long exampleId) {
        final Example example = exampleService.getExampleById(exampleId);
        if (example == null) {
            return ResponseBzn.fail("示例不存在");
        }
        final QueryExampleResponse exampleResponse = new QueryExampleResponse().setExampleId(example.getId())
                .setExampleCode(example.getExampleCode())
                .setExampleName(example.getExampleName());
        return ResponseBzn.ok(exampleResponse);
    }

    /**
     * 查询示例列表
     *
     * @param queryExamplePageRequest 请求参数
     * @return 响应结果
     */
    @GetMapping("/page")
    public ResponseBzn<PageListResponse<QueryExamplePageResponse>> queryExamplePage(
            @ModelAttribute QueryExamplePageRequest queryExamplePageRequest) {
        final QueryExamplePageParam queryExamplePageParam = new QueryExamplePageParam()
                .setExampleCode(queryExamplePageRequest.getExampleCode())
                .setExampleName(queryExamplePageRequest.getExampleName());
        queryExamplePageParam.setPageNumber(queryExamplePageRequest.getPageNumber());
        queryExamplePageParam.setPageSize(queryExamplePageRequest.getPageSize());
        final PageListResponse<ExamplePageBo> examplePage = exampleService.listExamplePage(queryExamplePageParam);
        final PageListResponse<QueryExamplePageResponse> examplePageResponse =
                examplePage.convertDataType(example -> new QueryExamplePageResponse()
                        .setExampleId(example.getId())
                        .setExampleCode(example.getExampleCode())
                        .setExampleName(example.getExampleName()));
        return ResponseBzn.ok(examplePageResponse);
    }

    /**
     * 查询示例集合
     *
     * @param queryExampleListRequest 请求参数
     * @return 响应结果
     */
    @GetMapping("/queryExampleList")
    public ResponseBzn<QueryExampleListResponse> queryExampleList(
            @ModelAttribute QueryExampleListRequest queryExampleListRequest) {
        final QueryExampleListParam queryExampleListParam = new QueryExampleListParam()
                .setExampleCode(queryExampleListRequest.getExampleCode())
                .setExampleName(queryExampleListRequest.getExampleName());
        final List<Example> examples = exampleService.listExample(queryExampleListParam);
        final List<QueryExampleListResponse.Example> exampleResponses = examples.stream().map(example ->
                new QueryExampleListResponse.Example()
                        .setExampleId(example.getId())
                        .setExampleCode(example.getExampleCode())
                        .setExampleName(example.getExampleName()))
                .collect(Collectors.toList());
        return ResponseBzn.ok(new QueryExampleListResponse().setExamples(exampleResponses));
    }

    /**
     * 添加示例
     *
     * @param addExampleRequest 请求参数
     * @return 响应结果
     */
    @PostMapping("/addExample")
    public ResponseBzn<AddExampleResponse> addExample(@RequestBody AddExampleRequest addExampleRequest) {
        // 数据校验
        if (StringUtils.isBlank(addExampleRequest.getExampleName())) {
            return ResponseBzn.fail("示例名称不能为空");
        }
        final AddExampleParam addExampleParam = new AddExampleParam()
                .setExampleName(addExampleRequest.getExampleName());
        final Long exampleId = exampleService.addExample(addExampleParam);
        return ResponseBzn.ok(new AddExampleResponse().setId(exampleId));
    }

    /**
     * 修改示例
     *
     * @param exampleId            示例ID
     * @param modifyExampleRequest 请求参数
     * @return 响应结果
     */
    @PutMapping("/{exampleId}")
    public ResponseBzn<?> modifyExample(@PathVariable("exampleId") Long exampleId,
                                        @RequestBody ModifyExampleRequest modifyExampleRequest) {
        // 数据校验
        if (StringUtils.isBlank(modifyExampleRequest.getExampleName())) {
            return ResponseBzn.fail("示例名称不能为空");
        }
        final Example example = exampleService.getExampleById(exampleId);
        if (example == null) {
            return ResponseBzn.fail("示例不存在");
        }

        final ModifyExampleParam modifyExampleParam = new ModifyExampleParam()
                .setExampleId(exampleId)
                .setExampleName(modifyExampleRequest.getExampleName());
        exampleService.modifyExample(modifyExampleParam);
        return ResponseBzn.ok();
    }

}