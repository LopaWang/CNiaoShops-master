package com.chhd.cniaoshops.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by CWQ on 2016/11/16.
 */

public class HomeCategory extends Category implements MultiItemEntity {

    private int imgBig;

    private int imgSmallTop;

    private int imgSmallBottom;

    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;

    private int itemType;

    public HomeCategory(String name, int imgBig, int imgSmallTop, int imgSmallBottom) {
        super(name);
        this.imgBig = imgBig;
        this.imgSmallTop = imgSmallTop;
        this.imgSmallBottom = imgSmallBottom;
    }

    public int getImgBig() {
        return imgBig;
    }

    public void setImgBig(int imgBig) {
        this.imgBig = imgBig;
    }

    public int getImgSmallTop() {
        return imgSmallTop;
    }

    public void setImgSmallTop(int imgSmallTop) {
        this.imgSmallTop = imgSmallTop;
    }

    public int getImgSmallBottom() {
        return imgSmallBottom;
    }

    public void setImgSmallBottom(int imgSmallBottom) {
        this.imgSmallBottom = imgSmallBottom;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
