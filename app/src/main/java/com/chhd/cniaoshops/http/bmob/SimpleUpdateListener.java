package com.chhd.cniaoshops.http.bmob;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.util.DialogUtils;
import com.chhd.cniaoshops.util.LoggerUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by CWQ on 2017/4/19.
 */

public abstract class SimpleUpdateListener extends UpdateListener implements Constant {

    private int delayMillis = DELAYMILLIS_FOR_RQUEST_FINISH;
    private long startTimeMillis;
    private Context progressDialog;
    private MaterialDialog dialog;


    public SimpleUpdateListener() {
    }



    public SimpleUpdateListener(Context progressDialog) {
        this.progressDialog = progressDialog;
        onBefore();
    }

    public final void onBefore() {
        startTimeMillis = System.currentTimeMillis();
        if (progressDialog != null && progressDialog instanceof Activity) {
            dialog = DialogUtils.newProgressDialog(progressDialog);
            dialog.show();
        }
        before();
    }

    @Override
    public void done(final BmobException e) {
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            if (e == null) {
                success();
            } else {
                LoggerUtils.e(e);
                error(e);
            }
            onAfter();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (e == null) {
                        success();
                    } else {
                        LoggerUtils.e(e);
                        error(e);
                    }
                    onAfter();
                }
            }, delayMillis - timeDif);
        }
    }

    public final void onAfter() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        after();
    }

    private long getTimeDif() {
        return System.currentTimeMillis() - startTimeMillis;
    }

    protected void before() {

    }

    public abstract void success();

    protected void error(BmobException e) {
        BmobEx.handlerError(e);
    }

    protected void after() {

    }
}
