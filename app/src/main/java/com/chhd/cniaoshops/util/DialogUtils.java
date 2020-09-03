package com.chhd.cniaoshops.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.activity.MainActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.SettingService;


public class DialogUtils {

    private DialogUtils() {
    }

    public static MaterialDialog.Builder newBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .titleColorRes(R.color.def_text_black)
                .contentColorRes(R.color.def_text_mid)
                .widgetColorRes(R.color.green_dialog)
                .positiveColorRes(R.color.green_dialog)
                .negativeColorRes(R.color.green_dialog)
                .neutralColorRes(R.color.green_dialog);
    }

    public static MaterialDialog newProgressDialog(Context context) {
        return newBuilder(context)
                .content(R.string.please_waiting)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .build();
    }


    public static MaterialDialog newRationaleDialog(Context context, final Rationale rationale) {
        return newBuilder(context)
                .title(R.string.permission_title_permission_rationale)
                .content(R.string.permission_message_permission_rationale)
                .positiveText(R.string.permission_resume)
                .negativeText(R.string.permission_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        rationale.resume();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        rationale.cancel();
                    }
                })
                .build();
    }

    public static MaterialDialog newDefineSettingDialog(Context context) {
        final SettingService settingService = AndPermission.defineSettingDialog((Activity) context, 0);
        return newBuilder(context)
                .title(R.string.permission_title_permission_failed)
                .content(R.string.permission_message_permission_failed)
                .positiveText(R.string.permission_setting)
                .negativeText(R.string.permission_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        settingService.execute();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        settingService.cancel();
                    }
                })
                .build();
    }
}
