package com.chhd.cniaoshops.bean;

/**
 * Created by CWQ on 2016/11/16.
 */

public class Category extends BaseBean {

    private String name;

    public Category(String name) {
        this.name = name;
    }

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
