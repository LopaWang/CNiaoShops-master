package com.chhd.cniaoshops.http;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.util.DialogUtils;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.request.BaseRequest;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by CWQ on 2017/4/22.
 */

public abstract class SimpleFileCallback extends FileCallback implements Constant {

    private boolean isToastError = false;
    private int delayMillis = DELAYMILLIS_FOR_RQUEST_FINISH;
    private long startTimeMillis;
    private Context progressDialog;
    private MaterialDialog dialog;

    public SimpleFileCallback(Context progressDialog) {
        this.progressDialog = progressDialog;
    }

    public SimpleFileCallback(String destFileName, Context progressDialog) {
        super(destFileName);
        this.progressDialog = progressDialog;
    }

    public SimpleFileCallback(String destFileDir, String destFileName, Context progressDialog) {
        super(destFileDir, destFileName);
        this.progressDialog = progressDialog;
    }

    @Override
    public final void onBefore(BaseRequest request) {
        super.onBefore(request);
        startTimeMillis = System.currentTimeMillis();
        if (progressDialog != null && progressDialog instanceof Activity) {
            LoggerUtils.i("---onBefore---progressDialog---");
            dialog = DialogUtils.newProgressDialog(progressDialog);
            dialog.show();
        }
        before(request);
    }

    @Override
    public final void onSuccess(final File file, final Call call, final Response response) {
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            success(file, call, response);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    success(file, call, response);
                }
            }, delayMillis - timeDif);
        }
    }

    @Override
    public final void onError(final Call call, final Response response, final Exception e) {
        super.onError(call, response, e);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            error(call, response, e);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    error(call, response, e);
                }
            }, delayMillis - timeDif);
        }
    }

    @Override
    public final void onAfter(final File file, final Exception e) {
        super.onAfter(file, e);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            after(file, e);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    after(file, e);
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }, delayMillis - timeDif);
        }
    }

    private long getTimeDif() {
        return System.currentTimeMillis() - startTimeMillis;
    }

    public void before(BaseRequest request) {

    }

    public abstract void success(File file, Call call, Response response);

    public void error(Call call, Response response, Exception e) {
        if (isToastError || progressDialog != null) {
            ToastyUtils.error(R.string.network_connect_fail);
        }
    }

    public void after(File file, Exception e) {

    }
}
