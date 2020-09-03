package com.chhd.cniaoshops.bean;

import java.io.Serializable;

/**
 * Created by CWQ on 2017/4/3.
 */

public class ShoppingCart extends Wares implements Serializable {

    private int count;

    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
