package com.mango.example.dao;

import com.mango.example.domain.bo.ExamplePageBo;
import com.mango.example.domain.model.Example;
import com.mango.example.domain.param.ModifyExampleParam;
import com.mango.example.domain.param.QueryExampleListParam;
import com.mango.example.domain.param.QueryExamplePageParam;
import com.mango.utils.tkmybatis.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 示例数据访问接口
 *
 */
@Repository
public interface ExampleMapper extends BaseMapper<Example> {

    /**
     * 根据示例ID获取示例
     *
     * @param exampleId 示例ID
     * @return 示例
     */
    Example getExampleById(Long exampleId);

    /**
     * 查询示例列表
     *
     * @param queryExamplePageParam 请求参数
     * @return 示例列表
     */
    List<ExamplePageBo> listExamplePage(QueryExamplePageParam queryExamplePageParam);

    /**
     * 查询示例集合
     *
     * @param queryExampleListParam 请求参数
     * @return 示例集合
     */
    List<Example> listExample(QueryExampleListParam queryExampleListParam);

    /**
     * 修改示例
     *
     * @param modifyExampleParam 修改参数
     * @return 修改条数
     */
    int updateExample(ModifyExampleParam modifyExampleParam);
}