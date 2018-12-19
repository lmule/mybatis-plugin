package com.scaffold.bootservice.controller;

import com.scaffold.bootservice.model.PageTypeModel;
import com.scaffold.bootservice.service.PageTypeService;
import ibatisData.ff.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page_type")
public class PageTypeController extends BaseController<PageTypeService, PageTypeModel> {
}