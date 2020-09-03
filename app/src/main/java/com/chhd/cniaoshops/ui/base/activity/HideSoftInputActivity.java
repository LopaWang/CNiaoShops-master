package com.chhd.cniaoshops.ui.base.activity;

import android.os.IBinder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public abstract class HideSoftInputActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 触摸EditText以外的地方隐藏软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                if (isShouldHideSoftInput(view, ev)) {
                    hideSoftInput(view.getWindowToken());
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断是否隐藏软键盘
     *
     * @param view
     * @param event
     * @return
     */
    protected boolean isShouldHideSoftInput(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] outLocation = {0, 0};
            view.getLocationInWindow(outLocation);
            int left = outLocation[0];
            int top = outLocation[1];
            int right = left + view.getWidth();
            int bottom = top + view.getHeight();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软键盘
     *
     * @param token
     */
    protected void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
