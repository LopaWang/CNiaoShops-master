package com.chhd.cniaoshops.bean;

/**
 * Created by CWQ on 2016/10/24.
 */
public class Tab {

    private int icon;

    private String title;

    private Class fragment;

    public Tab() {
    }

    public Tab(int icon, String title, Class fragment) {
        this.icon = icon;
        this.title = title;
        this.fragment = fragment;
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

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
