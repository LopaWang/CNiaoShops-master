package com.chhd.cniaoshops.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by CWQ on 2017/4/7.
 */

public class TouchEventViewPager extends ViewPager {

    float startX = 0;
    float startY = 0;

    public TouchEventViewPager(Context context) {
        this(context, null);
    }

    public TouchEventViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();

                float disX = moveX - startX;
                float disY = moveY - startY;

                if (Math.abs(disY) > Math.abs(disX) * 2) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}
