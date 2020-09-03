package com.chhd.per_library.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    private static final String NAME = "config";

    private static SharedPreferences sharedPreferences;

    private SpUtils() {
    }

    public static boolean getBoolean(String key, boolean defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = UiUtils.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        if (sharedPreferences == null) {
            sharedPreferences = UiUtils.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = UiUtils.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key, defValue);
    }

    public static void putInt(String key, int value) {
        if (sharedPreferences == null) {
            sharedPreferences = UiUtils.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static long getLong(String key, long defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = UiUtils.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getLong(key, defValue);
    }

    public static void putLong(String key, long value) {
        if (sharedPreferences == null) {
            sharedPreferences = UiUtils.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putLong(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = UiUtils.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, defValue);
    }

    public static void putString(String key, String value) {
        if (sharedPreferences == null) {
            sharedPreferences = UiUtils.getContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putString(key, value).commit();
    }

}
