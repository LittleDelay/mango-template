package com.mango.api.example.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 查询示例列表响应结果
 *
 */
@Data
@Accessors(chain = true)
public class QueryExamplePageRes implements Serializable {

    private static final long serialVersionUID = -8472606216095059514L;

    /**
     * 示例ID
     */
    private Long exampleId;

    /**
     * 示例Code
     */
    private String exampleCode;

    /**
     * 示例名称
     */
    private String exampleName;

}
