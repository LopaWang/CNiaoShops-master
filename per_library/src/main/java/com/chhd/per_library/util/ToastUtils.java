package com.chhd.per_library.util;

import android.widget.Toast;

public class ToastUtils {

    private static Toast toast;

    private ToastUtils() {
    }

    public static void makeText(CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(UiUtils.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }

    public static void makeText(int resId) {
        if (toast == null) {
            toast = Toast.makeText(UiUtils.getContext(), resId, Toast.LENGTH_SHORT);
        }
        toast.setText(resId);
        toast.show();
    }
}
