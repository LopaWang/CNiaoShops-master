package com.chhd.cniaoshops.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chhd.cniaoshops.ui.base.activity.BaseActivity;

/**
 * Created by Andy on 2017/3/10.
 */

/**
 * 监听是否切换系统语言，关闭所有Activity
 */
public class LocalChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {

            for (Activity activity : BaseActivity.activities) {
                activity.finish();
            }

        }
    }
}
