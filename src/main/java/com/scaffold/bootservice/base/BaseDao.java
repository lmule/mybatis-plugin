package com.scaffold.bootservice.base;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 扩展通用Mapper
 * @param <M>
 */
@RegisterMapper
public interface BaseDao<M> extends Mapper<M> {
}
