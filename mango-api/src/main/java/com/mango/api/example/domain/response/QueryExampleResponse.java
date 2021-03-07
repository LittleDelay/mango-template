package com.mango.api.example.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 查询示例响应结果
 */
@Data
@Accessors(chain = true)
public class QueryExampleResponse implements Serializable {

    private static final long serialVersionUID = -5116720258692282563L;

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
