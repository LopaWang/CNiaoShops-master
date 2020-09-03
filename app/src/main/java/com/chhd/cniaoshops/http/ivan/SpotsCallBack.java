package com.chhd.cniaoshops.http.ivan;

import android.content.Context;

import com.chhd.cniaoshops.R;
import com.chhd.per_library.util.UiUtils;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

public abstract class SpotsCallBack<T> extends BaseCallback<T> {

    private Context mContext;
    private SpotsDialog mDialog;

    public SpotsCallBack(Context context) {
        mContext = context;
        initSpotsDialog();
    }

    private void initSpotsDialog() {
        mDialog = new SpotsDialog(mContext, UiUtils.getString(R.string.loading));
    }

    public void showDialog() {
        mDialog.show();
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }

    public void setLoadMessage(int resId) {
        mDialog.setMessage(mContext.getString(resId));
    }

    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();
    }

    @Override
    public void onBeforeRequest(Request request) {

        showDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
