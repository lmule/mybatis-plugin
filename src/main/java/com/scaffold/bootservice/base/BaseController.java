package com.scaffold.bootservice.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public class BaseController<S extends BaseService, M extends BaseModel> {

    @Autowired
    S baseService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Object insert(@RequestBody M model) {
        baseService.insert(model);
        return model;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateById(@RequestBody M model) {
        baseService.updateById(model);
        return 0;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object findById(@RequestParam Integer id) {
        return baseService.findById(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object getListByPage(@RequestParam Map<String,String> requestMap) {
        List<M> list = baseService.findAll();
        return list;
    }
}
