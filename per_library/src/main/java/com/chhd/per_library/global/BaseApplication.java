package com.chhd.per_library.global;

import android.app.Application;
import android.content.Context;

/**
 * Created by CWQ on 2017/4/3.
 */

public class BaseApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }
}
