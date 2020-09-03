package com.chhd.cniaoshops.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.chhd.cniaoshops.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * Created by CWQ on 2017/4/10.
 */

public class EmptyView extends RelativeLayout {

    private LottieAnimationView animationView;
    private AVLoadingIndicatorView loadingIndicatorView;
    private TextView tvNone;
    private TextView tvLoading;
    private TextView tvError;

    private OnClickListener networkErrorClickListener;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_empty, this, true);
        setVisibility(GONE);
        animationView = (LottieAnimationView) findViewById(R.id.empty_animation_view);
        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator_view);
        tvNone = (TextView) findViewById(R.id.tv_none);
        tvLoading = (TextView) findViewById(R.id.tv_loading);
        tvError = (TextView) findViewById(R.id.tv_error);
    }

    public void showLoadView() {
        setVisibility(VISIBLE);
        animationView.setVisibility(View.INVISIBLE);
        loadingIndicatorView.setVisibility(View.VISIBLE);
        tvNone.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        tvLoading.setVisibility(View.VISIBLE);
    }

    public <T> void setEmptyView(List<T> data) {
        setEmptyView(data, null);
    }

    public <T> void setEmptyView(OnClickListener networkErrorClickListener) {
        setEmptyView(null, networkErrorClickListener);
    }

    private <T> void setEmptyView(List<T> data, final OnClickListener networkErrorClickListener) {
        int visibility = data == null || data.size() == 0 ? View.VISIBLE : View.GONE;
        setVisibility(visibility);
        animationView.setVisibility(View.VISIBLE);
        loadingIndicatorView.setVisibility(View.INVISIBLE);
        tvLoading.setVisibility(View.GONE);
        if (networkErrorClickListener != null) {
            tvNone.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadView();
                    if (networkErrorClickListener != null) {
                        networkErrorClickListener.onClick(v);
                    }
                }
            });
        } else {
            tvNone.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
        }
        if (animationView != null) {
            if ((data == null || data.size() == 0) && !animationView.isAnimating()) {
                animationView.playAnimation();
            } else if (data != null && data.size() > 0 && animationView.isAnimating()) {
                animationView.cancelAnimation();
            }
        }
    }
}
