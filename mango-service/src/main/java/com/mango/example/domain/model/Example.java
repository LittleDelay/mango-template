package com.mango.example.domain.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 示例信息实体
 *
 */
@Data
@Table(name = "t_example")
public class Example implements Serializable {

    private static final long serialVersionUID = 7603425979405112881L;

    /**
     * 示例ID
     */
    @Column(name = "id")
    private Long id;

    /**
     * 示例Code
     */
    @Column(name = "example_code")
    private String exampleCode;

    /**
     * 示例名称
     */
    @Column(name = "example_name")
    private String exampleName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}