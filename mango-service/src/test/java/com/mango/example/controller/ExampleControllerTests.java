package com.mango.example.controller;

import com.mango.BackendServiceApplicationTests;
import com.mango.api.example.domain.request.AddExampleReq;
import com.mango.api.example.domain.request.ModifyExampleReq;
import com.mango.api.example.service.ExampleService;
import com.mango.utils.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 对于 Controller 层的接口单元测试类
 *
 */
@Slf4j
@AutoConfigureMockMvc
public class ExampleControllerTests extends BackendServiceApplicationTests {

    /**
     * 这里我们使用 MockMvc 的方式进行单元测试
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * 查询示例信息
     */
    @Test
    void queryExample() throws Exception {
        String url = ExampleService.BASE_URL + "/{exampleId}";

        // MockMvcResultHandlers.print() 会打印详细的请求与响应信息
        ResultActions actions = this.mockMvc.perform(
                MockMvcRequestBuilders.get(url, 430809380267626496L)).andDo(MockMvcResultHandlers.print());

        // 一般情况下，如果我们不需要对返回数据做处理，上面的代码即可完成单元测试结果
        // 如果需要对返回数据做进一步处理，可以通过以下方式获取接口返回的数据（不包含请求与响应信息）
        MvcResult result = actions.andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();

        log.info("查询示例信息返回的数据：{}", content);
    }

    /**
     * 分页查询示例信息列表
     */
    @Test
    void queryExamplePage() throws Exception {
        String url = ExampleService.BASE_URL + "/page";

        ResultActions actions = this.mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("pageNumber", "1")
                        .param("pageSize", "5")
                        .param("exampleName", "示例"))
                .andDo(MockMvcResultHandlers.print());

        String content = actions.andReturn().getResponse().getContentAsString();
        log.info("分页查询示例信息列表返回数据：{}", content);
    }

    /**
     * 查询示例信息列表
     */
    @Test
    void queryExampleList() throws Exception {
        String url = ExampleService.BASE_URL + "/queryExampleList";

        ResultActions actions = this.mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("exampleCode", "P00000000000028328"))
                .andDo(MockMvcResultHandlers.print());

        String content = actions.andReturn().getResponse().getContentAsString();
        log.info("查询示例信息列表返回数据：{}", content);
    }

    /**
     * 添加示例信息
     */
    @Test
    void addExample() throws Exception {
        String url = ExampleService.BASE_URL + "/addExample";

        AddExampleReq addExampleReq = new AddExampleReq();
        addExampleReq.setExampleName("测试示例名称");

        ResultActions actions = this.mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .content(Objects.requireNonNull(JsonUtil.toJsonIgnoreNull(addExampleReq))))
                .andDo(MockMvcResultHandlers.print());

        String content = actions.andReturn().getResponse().getContentAsString();
        log.info("添加示例信息返回的数据：{}", content);
    }

    /**
     * 修改示例信息
     */
    @Test
    void modifyExample() throws Exception {
        String url = ExampleService.BASE_URL + "/{exampleId}";

        ModifyExampleReq modifyExampleReq = new ModifyExampleReq();
        modifyExampleReq.setExampleName("测试修改后的示例名称");

        ResultActions actions = this.mockMvc.perform(
                MockMvcRequestBuilders.put(url, 459003288034086912L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .content(Objects.requireNonNull(JsonUtil.toJsonIgnoreNull(modifyExampleReq))))
                .andDo(MockMvcResultHandlers.print());

        String content = actions.andReturn().getResponse().getContentAsString();
        log.info("修改示例信息返回的数据：{}", content);
    }

}