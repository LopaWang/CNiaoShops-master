package com.chhd.cniaoshops.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by CWQ on 2017/4/3.
 */

public class JsonUtils {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private JsonUtils() {
    }

    public static <T> T fromJson(String json, Type type) {

        return gson.fromJson(json, type);
    }

    public static String toJSON(Object object) {

        return gson.toJson(object);
    }
}
