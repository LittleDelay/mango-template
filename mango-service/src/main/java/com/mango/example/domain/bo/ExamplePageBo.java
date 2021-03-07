package com.mango.example.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 示例列表实体
 *
 */
@Data
@Accessors(chain = true)
public class ExamplePageBo implements Serializable {

    private static final long serialVersionUID = -3310009647392453260L;

    /**
     * 示例ID
     */
    private Long id;

    /**
     * 示例Code
     */
    private String exampleCode;

    /**
     * 示例名称
     */
    private String exampleName;
}
