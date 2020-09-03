package com.chhd.cniaoshops.http;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.global.Config;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.util.DialogUtils;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import java.nio.charset.Charset;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

import static com.alibaba.fastjson.util.IOUtils.UTF8;

/**
 * Created by CWQ on 2017/4/9.
 */

public abstract class SimpleStringCallback extends StringCallback implements Constant {

    private boolean isToastError = false;
    private int delayMillis = DELAYMILLIS_FOR_RQUEST_FINISH;
    private long startTimeMillis;
    private Context progressDialog;
    private MaterialDialog dialog;
    private Request request;

    @Override
    public final void onBefore(Request request, int id) {
        super.onBefore(request, id);
        this.request = request;
        startTimeMillis = System.currentTimeMillis();
        if (progressDialog != null && progressDialog instanceof Activity) {
            dialog = DialogUtils.newProgressDialog(progressDialog);
            dialog.show();
        }
        before(request, id);
    }

    @Override
    public final void onResponse(final String response, final int id) {
        d(request, response);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            success(response, id);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    success(response, id);
                }
            }, delayMillis - timeDif);
        }
    }

    @Override
    public final void onError(final Call call, final Exception e, final int id) {
        e(request, e);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            error(call, e, id);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    error(call, e, id);
                }
            }, delayMillis - timeDif);
        }
    }

    @Override
    public final void onAfter(final int id) {
        super.onAfter(id);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            after(id);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    after(id);
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

    public void before(Request request, int id) {

    }

    public abstract void success(String response, int id);

    public void error(Call call, Exception e, int id) {
        if (isToastError|| progressDialog != null) {
            ToastyUtils.error(R.string.network_connect_fail);
        }
    }

    public void after(int id) {

    }

    private void d(Request request, String json) {
        if (Config.isDebug) {
            String paramsStr = request.body() != null ? "params:\t" + formatParamsStr(getParams(request)) : "";
            String message =
                    "url:\t\t" + request.url().toString()
                            + "\n\n"
                            + paramsStr
                            + "\n\n"
                            + "json:\t" + json;
            Logger.d(message);
        }
    }

    private void e(Request request, Throwable throwable) {
        if (Config.isDebug) {
            String paramsStr = request.body() != null ? "params:\t" + formatParamsStr(getParams(request))  : "";
            String message =
                    "url:\t\t" + request.url().toString()
                            + "\n\n"
                            + paramsStr
                            + "\n\n"
                            + ERROR;
            Logger.e(throwable, message);
        }
    }

    private String formatParamsStr(String params) {
        return params.replace("&", "\n" + "params:\t");
    }

    private String getParams(Request request) {
        try {
            RequestBody requestBody = request.body();
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            String paramsStr = buffer.readString(charset);
            return paramsStr;
        } catch (Exception e) {
            LoggerUtils.e(e);
        }
        return "";
    }
}
