package com.mango.example.service.impl;

import com.mango.common.service.IdGenerationService;
import com.mango.example.dao.ExampleMapper;
import com.mango.example.domain.model.Example;
import com.mango.example.domain.param.AddExampleParam;
import com.mango.example.domain.param.ModifyExampleParam;
import com.mango.example.domain.param.QueryExampleListParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 示例接口实现类
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ExampleServiceImpl {

    @Autowired
    private ExampleMapper exampleMapper;

    @Autowired
    private IdGenerationService idGenerationService;

    /**
     * 根据示例ID获取示例
     *
     * @param exampleId 示例ID
     * @return 示例
     */
    public Example getExampleById(Long exampleId) {
        return exampleMapper.getExampleById(exampleId);
    }

    /**
     * 查询示例集合
     *
     * @param queryExampleListParam 请求参数
     * @return 示例集合
     */
    public List<Example> listExample(QueryExampleListParam queryExampleListParam) {
        return exampleMapper.listExample(queryExampleListParam);
    }

    /**
     * 添加示例
     *
     * @param addExampleParam 请求参数
     * @return 示例ID
     */
    public Long addExample(AddExampleParam addExampleParam) {
        Example example = new Example();
        example.setId(idGenerationService.getPkId());
        example.setExampleName(addExampleParam.getExampleName());
        return example.getId();
    }

    /**
     * 修改示例
     *
     * @param modifyExampleParam 请求参数
     */
    public void modifyExample(ModifyExampleParam modifyExampleParam) {
        exampleMapper.updateExample(modifyExampleParam);
    }
}
