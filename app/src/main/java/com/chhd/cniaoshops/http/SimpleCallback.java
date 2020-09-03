package com.chhd.cniaoshops.http;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.global.Config;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.util.DialogUtils;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.orhanobut.logger.Logger;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by CWQ on 2017/3/26.
 */

public abstract class SimpleCallback extends StringCallback implements Constant {

    private boolean isToastError = false;
    private int delayMillis = DELAYMILLIS_FOR_RQUEST_FINISH;
    private long startTimeMillis;
    private Context progressDialog;
    private MaterialDialog dialog;
    private BaseRequest request;

    public SimpleCallback() {
    }

    public SimpleCallback(boolean isToastError) {
        this.isToastError = isToastError;
    }

    public SimpleCallback(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public SimpleCallback(Context progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    public final void onBefore(BaseRequest request) {
        super.onBefore(request);
        this.request = request;
        startTimeMillis = System.currentTimeMillis();
        if (progressDialog != null && progressDialog instanceof Activity) {
            dialog = DialogUtils.newProgressDialog(progressDialog);
            dialog.show();
        }
        before(request);
    }

    @Override
    public final void onCacheSuccess(String s, Call call) {
        super.onCacheSuccess(s, call);
        d(request, s);
        cacheSuccess(s, call);
    }

    @Override
    public final void onSuccess(String s, Call call, Response response) {
        d(request, s);
        delayExcute(new SuccessRun(s, call, response));
    }

    private class SuccessRun implements Runnable {

        private String s;
        private Call call;
        private Response response;

        public SuccessRun(String s, Call call, Response response) {
            this.s = s;
            this.call = call;
            this.response = response;
        }

        @Override
        public void run() {
            success(s, call, response);
        }
    }

    @Override
    public final void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        e(request, response, e);
        delayExcute(new ErroeRun(call, response, e));
    }

    private class ErroeRun implements Runnable {

        private Call call;
        private Response response;
        private Exception e;

        public ErroeRun(Call call, Response response, Exception e) {
            this.call = call;
            this.response = response;
            this.e = e;
        }

        @Override
        public void run() {
            error(call, response, e);
        }
    }

    @Override
    public final void onAfter(String s, Exception e) {
        super.onAfter(s, e);
        delayExcute(new AfterRun(s, e));
    }

    private class AfterRun implements Runnable {

        private String s;
        private Exception e;

        public AfterRun(String s, Exception e) {
            this.s = s;
            this.e = e;
        }

        @Override
        public void run() {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            after(s, e);
            if (!TextUtils.isEmpty(s)) {
                afterSuccess(s);
            }
            if (e != null) {
                afterError(e);
            }
        }
    }

    private void delayExcute(final Runnable r) {
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            new Handler().post(r);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Handler().post(r);
                }
            }, delayMillis - timeDif);
        }
    }

    private long getTimeDif() {
        return System.currentTimeMillis() - startTimeMillis;
    }

    public void before(BaseRequest request) {

    }

    public void cacheSuccess(String s, Call call) {

    }

    public abstract void success(String s, Call call, Response response);

    public void error(Call call, Response response, Exception e) {
        if (isToastError || progressDialog != null) {
            ToastyUtils.error(R.string.network_connect_fail);
        }
    }

    public void after(String s, Exception e) {
    }

    public void afterSuccess(String s) {

    }

    public void afterError(Exception e) {

    }

    /**
     * 打印OKGO请求成功信息
     */
    private void d(BaseRequest request, String json) {
        if (Config.isDebug) {
            String paramsStr = request.getParams() != null ? "params:\t" + formatParamsStr(request.getParams().toString()) : "";
            String message =
                    "url:\t\t" + request.getUrl()
                            + "\n\n"
                            + paramsStr
                            + "\n\n"
                            + "json:\t" + json;
            Logger.d(message);
        }
    }


    /**
     * 打印OKGO请求失败信息
     */
    private void e(BaseRequest request, Response response, Throwable throwable) {
        if (Config.isDebug) {
            String paramsStr = request.getParams() != null ? "params:\t" + formatParamsStr(request.getParams().toString()) + "\n\n" : "";
            String responseCode = response != null ? "responseCode:\t" + response.code() : "";
            String xml = response != null ? "xml:\t" + getXml(response) + "\n\n" : "";
            String message =
                    "url:\t\t" + request.getUrl()
                            + "\n\n"
                            + paramsStr
                            + "\n\n"
                            + responseCode
                            + xml
                            + ERROR;
            Logger.e(throwable, message);
        }
    }

    private String formatParamsStr(String params) {
        return params.replace("&", "\n" + "params:\t");
    }

    private String getXml(Response response) {
        try {
            return response.body().string();
        } catch (Exception e) {
            LoggerUtils.e(e);
        }
        return "";
    }

}
