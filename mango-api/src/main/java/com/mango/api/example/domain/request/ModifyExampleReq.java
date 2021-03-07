package com.mango.api.example.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 修改示例请求参数
 */
@Data
@Accessors(chain = true)
public class ModifyExampleReq implements Serializable {

    private static final long serialVersionUID = 1073414575098200417L;

    /**
     * 示例名称
     */
    private String exampleName;
    
}
