package com.scaffold.bootservice.model.entity;

import java.util.Date;
import javax.persistence.Table;

/**
 * 页面: page_type
 */
@Table(name = "page_type")
public class PageType extends BaseModel {
    /**
     * 
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

    /**
     * 
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取姓名
     * @return 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 
     * @return
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}