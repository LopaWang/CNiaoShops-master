package com.chhd.cniaoshops.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.chhd.per_library.util.UiUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

/**
 * Created by CWQ on 2017/4/6.
 */

public class SwipeDelRecyclerView extends RecyclerView {

    private float mDownX;
    private float mDownY;

    public SwipeDelRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeDelRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeDelRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (Math.abs(dy) > UiUtils.dp2px(5)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
            if (null != viewCache) {
                viewCache.smoothClose();
            }
        }
        return super.onTouchEvent(e);
    }
}
