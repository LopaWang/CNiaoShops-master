package com.chhd.per_library.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andy on 2016/10/22.
 */
public class SoftKeyboardUtils {

    private SoftKeyboardUtils() {
    }

    /**
     * 自动弹出软键盘，并将光标定位到末尾
     *
     * @param editText
     */
    public static void showSoftInput(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);

        editText.setSelection(editText.getText().toString().length());
    }
}
