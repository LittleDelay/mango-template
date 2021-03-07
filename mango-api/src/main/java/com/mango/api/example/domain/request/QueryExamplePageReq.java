package com.mango.api.example.domain.request;

import com.mango.core.bean.request.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询示例列表请求参数
 *
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class QueryExamplePageReq extends PageReq {

    private static final long serialVersionUID = -1160350803271063814L;

    /**
     * 示例Code（精确查询）
     */
    private String exampleCode;

    /**
     * 示例名称（模糊查询）
     */
    private String exampleName;

}
