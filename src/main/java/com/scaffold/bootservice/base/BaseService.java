package com.scaffold.bootservice.base;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseService<D extends BaseDao<M>, M extends BaseModel> {

    @Autowired
    protected D dao;

    private Class<M> modelClass;

    private BaseService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<M>) pt.getActualTypeArguments()[1];
    }

    public void insert(M model) {
        dao.insertSelective(model);
    }

    public void updateById(M model) {
        dao.updateByPrimaryKeySelective(model);
    }

    public void deleteById(Integer id) {
        dao.deleteByPrimaryKey(id);
    }

    public M findById(Integer id) {
        return dao.selectByPrimaryKey(id);
    }

    public List<M> findAll() {
        Example example = new Example(modelClass);
        example.setOrderByClause("id desc");
        return dao.selectByExample(example);
    }
//
//    public List<M> findByCondition(M model) {
//        return dao.select(model);
//    }
}
