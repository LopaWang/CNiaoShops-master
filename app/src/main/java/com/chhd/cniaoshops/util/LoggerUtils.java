package com.chhd.cniaoshops.util;

import com.chhd.cniaoshops.global.Config;
import com.chhd.cniaoshops.global.Constant;
import com.lzy.okgo.model.HttpParams;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by CWQ on 2016/11/2.
 */
public class LoggerUtils implements Constant {

    private static boolean isDebug = Config.isDebug;

    private LoggerUtils() {

    }

    public static void v(String message) {
        if (isDebug) {
            Logger.v(message);
        }
    }

    public static void d(String message) {
        if (isDebug) {
            Logger.d(message);
        }
    }

    public static void i(String message) {
        if (true) {
            Logger.i(message);
        }
    }

    public static void e(Throwable throwable) {
        if (true) {
            Logger.e(throwable, "error");
        }
    }

    public static void e(String message) {
        if (isDebug) {
            Logger.e(message);
        }
    }


}
