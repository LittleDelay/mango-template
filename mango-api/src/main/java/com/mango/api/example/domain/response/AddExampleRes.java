package com.mango.api.example.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 添加示例响应结果
 *
 */
@Data
@Accessors(chain = true)
public class AddExampleRes implements Serializable {

    private static final long serialVersionUID = 6849249272373367392L;

    /**
     * 示例ID
     */
    private Long id;
    
}
