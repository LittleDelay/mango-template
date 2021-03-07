package com.mango.utils.tkmybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 继承自己的BaseMapper
 *
 * @author Shangxp
 * @version 1.0.0
 * @since 2019/9/19 14:56
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

    // TODO
    // FIXME 特别注意，该接口不能被扫描到，否则会出错

}