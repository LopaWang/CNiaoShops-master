package com.chhd.cniaoshops.ui.widget.header;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;

/**
 * Created by CWQ on 2017/4/15.
 */

public class SinaRefreshView extends FrameLayout implements IHeaderView {

    private ImageView refreshArrow;
    private ImageView loadingView;
    private TextView refreshTextView;

    public SinaRefreshView(Context context) {
        this(context, null);
    }

    public SinaRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SinaRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), com.lcodecore.tkrefreshlayout.R.layout.view_sinaheader, null);
        refreshArrow = (ImageView) rootView.findViewById(com.lcodecore.tkrefreshlayout.R.id.iv_arrow);
        refreshTextView = (TextView) rootView.findViewById(com.lcodecore.tkrefreshlayout.R.id.tv);
        loadingView = (ImageView) rootView.findViewById(com.lcodecore.tkrefreshlayout.R.id.iv_loading);
        addView(rootView);
    }

    public void setArrowResource(@DrawableRes int resId) {
        refreshArrow.setImageResource(resId);
    }

    public void setTextColor(@ColorInt int color) {
        refreshTextView.setTextColor(color);
    }

    public void setPullDownStr(String pullDownStr1) {
        pullDownStr = pullDownStr1;
    }

    public void setReleaseRefreshStr(String releaseRefreshStr1) {
        releaseRefreshStr = releaseRefreshStr1;
    }

    public void setRefreshingStr(String refreshingStr1) {
        refreshingStr = refreshingStr1;
    }

    private String pullDownStr = "下拉刷新";
    private String releaseRefreshStr = "释放刷新";
    private String refreshingStr = "正在刷新";

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) refreshTextView.setText(pullDownStr);
        if (fraction > 1f) refreshTextView.setText(releaseRefreshStr);
        refreshArrow.setVisibility(VISIBLE);
        refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
//        if (fraction < 1f) {
//            refreshTextView.setText(pullDownStr);
//            refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
//            if (refreshArrow.getVisibility() == INVISIBLE) {
//                refreshArrow.setVisibility(VISIBLE);
//                loadingView.setVisibility(INVISIBLE);
//            }
//        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        refreshTextView.setText(refreshingStr);
        refreshArrow.setVisibility(INVISIBLE);
        loadingView.setVisibility(VISIBLE);
        ((AnimationDrawable) loadingView.getDrawable()).start();
    }

    @Override
    public void onFinish(final OnAnimEndListener listener) {
        loadingView.setVisibility(INVISIBLE);
        refreshArrow.setVisibility(INVISIBLE);
        refreshTextView.setText("刷新成功");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onAnimEnd();
            }
        }, 500);
    }

    @Override
    public void reset() {
        refreshArrow.setVisibility(VISIBLE);
        loadingView.setVisibility(INVISIBLE);
        refreshTextView.setText(pullDownStr);
    }
}
