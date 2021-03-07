package com.mango.example.domain.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 修改示例请求参数
 *
 */
@Data
@Accessors(chain = true)
public class ModifyExampleParam implements Serializable {

    private static final long serialVersionUID = -5637797031722435071L;

    /**
     * 示例ID
     */
    private Long exampleId;

    /**
     * 示例名称
     */
    private String exampleName;
}
