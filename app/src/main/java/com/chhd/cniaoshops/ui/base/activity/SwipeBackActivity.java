package com.chhd.cniaoshops.ui.base.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chhd.cniaoshops.ui.widget.SwipeBackLayout;


public abstract class SwipeBackActivity extends BaseActivity implements SwipeBackLayout.SwipeBackListener {

    //    private static final SwipeBackLayout.DragEdge DEFAULT_DRAG_EDGE = SwipeBackLayout.DragEdge.TOP;
    private static final SwipeBackLayout.Direction DIRECTION = SwipeBackLayout.Direction.VISIBLE;

    private SwipeBackLayout swipeBackLayout;
    private ImageView ivShadow;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(getContainer());
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        swipeBackLayout.addView(view);

    }

    private View getContainer() {
        RelativeLayout container = new RelativeLayout(this);
        swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.setDirection(DIRECTION);
        swipeBackLayout.setOnSwipeBackListener(this);
        ivShadow = new ImageView(this);
        ivShadow.setBackgroundColor(getResources().getColor(com.liuguangqiang.swipeback.R.color.black_p50));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        container.addView(ivShadow, params);
        container.addView(swipeBackLayout);
        return container;
    }

    public void setEnableSwipe(boolean enableSwipe) {
        swipeBackLayout.setEnablePullToBack(enableSwipe);
    }

    public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
        swipeBackLayout.setDragEdge(dragEdge);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackLayout;
    }

    @Override
    public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
        ivShadow.setAlpha(1 - fractionScreen);
    }
}
