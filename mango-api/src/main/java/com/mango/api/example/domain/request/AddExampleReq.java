package com.mango.api.example.domain.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 添加示例请求参数
 *
 */
@Data
@Accessors(chain = true)
public class AddExampleReq implements Serializable {

    private static final long serialVersionUID = 4618912115519137228L;

    /**
     * 示例名称
     */
    private String exampleName;
    
}
