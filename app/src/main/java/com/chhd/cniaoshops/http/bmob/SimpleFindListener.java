package com.chhd.cniaoshops.http.bmob;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.util.DialogUtils;
import com.chhd.cniaoshops.util.LoggerUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by CWQ on 2017/5/1.
 */

public abstract class SimpleFindListener<T> extends FindListener<T> implements Constant {

    private int delayMillis = DELAYMILLIS_FOR_RQUEST_FINISH;
    private long startTimeMillis;
    private Context progressDialog;
    private MaterialDialog dialog;

    public SimpleFindListener() {
        onBefore();
    }

    public SimpleFindListener(Context progressDialog) {
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
    public void done(List<T> list, BmobException e) {
        delayExcute(new DoneRun(list, e));
    }

    private class DoneRun implements Runnable {

        private List<T> list;
        private BmobException e;

        public DoneRun(List<T> list, BmobException e) {
            this.list = list;
            this.e = e;
        }

        @Override
        public void run() {
            if (e == null) {
                success(list);
            } else {
                LoggerUtils.e(e);
                error(e);
            }
            onAfter();
        }
    }

    public final void onAfter() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        after();
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

    protected void before() {

    }

    public abstract void success(final List<T> list);

    protected void error(BmobException e) {
        BmobEx.handlerError(e);
    }

    protected void after() {

    }
}
