package com.chhd.per_library.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.chhd.per_library.global.BaseApplication;

/**
 * Created by CWQ on 2016/10/28.
 */
public class UiUtils {

    private UiUtils() {
    }

    public static Context getContext() {
        return BaseApplication.context;
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static int dp2px(float dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int getScreenWidth() {
        int widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }

    public static String getString(int resId) {
        return getContext().getString(resId);
    }

    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    public static int getColor(int id) {
        return getResources().getColor(id);
    }

    public static int getTextSize(int id) {
        TypedValue value = new TypedValue();
        getResources().getValue(id, value, true);
        return (int) TypedValue.complexToFloat(value.data);
    }

    public static int getStatusBarHeight() {

        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
