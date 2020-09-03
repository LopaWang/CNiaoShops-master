package com.chhd.cniaoshops.ui.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * Created by CWQ on 2017/3/26.
 */

public abstract class SimpleAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> implements Constant {

    protected RecyclerView recyclerView;
    private Context context;

    private LottieAnimationView animationView;
    private AVLoadingIndicatorView loadingIndicatorView;
    private View emptyView;
    private TextView tvNone;
    private TextView tvError;
    private TextView tvLoading;

    public SimpleAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public SimpleAdapter(RecyclerView recyclerView, List<T> data) {
        super(data);
        this.recyclerView = recyclerView;
        this.context = recyclerView.getContext();
        openLoadAnimation();
        initEmptyView(context);
    }

    public SimpleAdapter(RecyclerView recyclerView, int layoutResId, List<T> data) {
        super(layoutResId, data);
        this.recyclerView = recyclerView;
        this.context = recyclerView.getContext();
        openLoadAnimation();
        initEmptyView(context);
    }

    private void initEmptyView(Context context) {
        emptyView = View.inflate(context, R.layout.view_empty, null);
        animationView = (LottieAnimationView) emptyView.findViewById(R.id.empty_animation_view);
        loadingIndicatorView = (AVLoadingIndicatorView) emptyView.findViewById(R.id.loading_indicator_view);
        tvNone = (TextView) emptyView.findViewById(R.id.tv_none);
        tvLoading = (TextView) emptyView.findViewById(R.id.tv_loading);
        tvError = (TextView) emptyView.findViewById(R.id.tv_error);
    }

    public void refreshData(List<T> data) {
        getData().clear();
        getData().addAll(data);
        notifyDataSetChanged();
        if (isLoadMoreEnable()) {
            loadMoreComplete();
        }
    }

    public void addAll(final List<T> data) {
        getData().addAll(data);
        notifyDataSetChanged();
        if (data != null && !data.isEmpty()) {
            if (isLoadMoreEnable()) {
                loadMoreComplete();
            }
        } else {
            if (isLoadMoreEnable()) {
                loadMoreEnd();
            } else {
                ToastyUtils.warning(R.string.no_to_more);
            }
        }
    }

    @Override
    public void remove(int position) {
        super.remove(position);
        LoggerUtils.i("size: " + getData().size());
        if (animationView != null) {
            if (getData().size() == 0 && !animationView.isAnimating()) {
                animationView.playAnimation();
            } else if (getData().size() > 0 && animationView.isAnimating()) {
                animationView.cancelAnimation();
            }
        }
    }

    public void setLoadingView() {
        if (recyclerView.getHeight() != 0) {
            showLoadingView();
            setEmptyView(emptyView);
        } else {
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    showLoadingView();
                    setEmptyView(emptyView);
                }
            });
        }
        if (animationView != null) {
            if (getData().size() == 0 && !animationView.isAnimating()) {
                animationView.playAnimation();
            } else if (getData().size() > 0 && animationView.isAnimating()) {
                animationView.cancelAnimation();
            }
        }
    }

    private void showLoadingView() {
        animationView.setVisibility(View.INVISIBLE);
        loadingIndicatorView.setVisibility(View.VISIBLE);
        tvNone.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        tvLoading.setVisibility(View.VISIBLE);
    }

    public void setCustomEmptyView() {
        if (recyclerView != null) {
            if (recyclerView.getHeight() != 0) {
                setNoneView();
            } else {
                recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        setNoneView();
                    }
                });
            }
        } else {
            setNoneView();
        }
        if (animationView != null) {
            if (getData().size() == 0 && !animationView.isAnimating()) {
                animationView.playAnimation();
            } else if (getData().size() > 0 && animationView.isAnimating()) {
                animationView.cancelAnimation();
            }
        }
    }

    public void setCustomEmptyView(final View.OnClickListener networkErrorClickListener) {
        if (recyclerView != null) {
            if (recyclerView.getHeight() != 0) {
                setErrorView(networkErrorClickListener);
            } else {
                recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        setErrorView(networkErrorClickListener);
                    }
                });
            }
        } else {
            setErrorView(networkErrorClickListener);
        }
        if (animationView != null) {
            if (getData().size() == 0 && !animationView.isAnimating()) {
                animationView.playAnimation();
            } else if (getData().size() > 0 && animationView.isAnimating()) {
                animationView.cancelAnimation();
            }
        }
    }

    private void setNoneView() {
        if (recyclerView != null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getEmptyViewHeight());
            emptyView.setLayoutParams(params);
        }
        animationView.setVisibility(View.VISIBLE);
        loadingIndicatorView.setVisibility(View.INVISIBLE);
        tvNone.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        setHeaderFooterEmpty(true, true);
        setEmptyView(emptyView);
    }

    private void setErrorView(final View.OnClickListener networkErrorClickListener) {
        if (recyclerView != null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getEmptyViewHeight());
            emptyView.setLayoutParams(params);
        }
        animationView.setVisibility(View.VISIBLE);
        loadingIndicatorView.setVisibility(View.INVISIBLE);
        tvNone.setVisibility(View.GONE);
        tvLoading.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        setHeaderFooterEmpty(true, true);
        setEmptyView(emptyView);
        if (networkErrorClickListener != null) {
            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadingView();
                    networkErrorClickListener.onClick(v);
                }
            });
        }
    }

    private int getEmptyViewHeight() {
        int headerLayoutHeight = 0;
        if (getHeaderLayout() != null) {
            headerLayoutHeight = getHeaderLayout().getHeight();
        }
        int footerLayoutHeight = 0;
        if (getFooterLayout() != null) {
            footerLayoutHeight = getFooterLayout().getHeight();
        }
        int height = (int) (recyclerView.getHeight() - headerLayoutHeight - footerLayoutHeight - recyclerView.getHeight() * 0.1);
        return height;
    }
}
