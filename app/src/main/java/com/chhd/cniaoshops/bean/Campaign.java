package com.chhd.cniaoshops.bean;

import java.io.Serializable;

/**
 * Created by CWQ on 2017/3/7.
 */

public class Campaign implements Serializable {

    private Long id;

    private String title;

    private String imgUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
