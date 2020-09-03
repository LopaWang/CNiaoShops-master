package com.chhd.cniaoshops.bean;

/**
 * Created by CWQ on 2017/4/13.
 */

public class Menu {

    private int icon;

    private String title;

    public Menu(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
