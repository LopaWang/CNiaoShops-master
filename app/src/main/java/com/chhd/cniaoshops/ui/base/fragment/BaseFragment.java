package com.chhd.cniaoshops.ui.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.global.App;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.ui.activity.LoginActivity;

import butterknife.ButterKnife;

/**
 * Created by CWQ on 2017/3/21.
 */

public abstract class BaseFragment extends Fragment implements Constant {

    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(getLayoutResID(), container, false);

        ButterKnife.bind(this, rootView);

        createView();

        return rootView;
    }

    protected void createView() {

    }

    public abstract int getLayoutResID();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView != null) {
            LottieAnimationView emptyAnimView = (LottieAnimationView) rootView.findViewById(R.id.empty_animation_view);
            if (isVisibleToUser && emptyAnimView != null && !emptyAnimView.isAnimating()) {
                emptyAnimView.resumeAnimation();
            } else if (!isVisibleToUser && emptyAnimView != null && emptyAnimView.isAnimating()) {
                emptyAnimView.pauseAnimation();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LottieAnimationView emptyAnimView = (LottieAnimationView) rootView.findViewById(R.id.empty_animation_view);
        if (emptyAnimView != null && !emptyAnimView.isAnimating() && !hidden) {
            emptyAnimView.resumeAnimation();
        } else if (emptyAnimView != null && emptyAnimView.isAnimating() && hidden) {
            emptyAnimView.pauseAnimation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LottieAnimationView emptyAnimView = (LottieAnimationView) rootView.findViewById(R.id.empty_animation_view);
        if (emptyAnimView != null && !emptyAnimView.isAnimating()) {
            emptyAnimView.resumeAnimation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LottieAnimationView emptyAnimView = (LottieAnimationView) rootView.findViewById(R.id.empty_animation_view);
        if (emptyAnimView != null && emptyAnimView.isAnimating()) {
            emptyAnimView.pauseAnimation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LottieAnimationView emptyAnimView = (LottieAnimationView) rootView.findViewById(R.id.empty_animation_view);
        if (emptyAnimView != null && emptyAnimView.isAnimating()) {
            emptyAnimView.cancelAnimation();
        }
    }

    protected void startActivity(Intent intent, boolean isRequireLogin) {
        if (isRequireLogin) {
            if (App.user != null) {
                super.startActivity(intent);
            } else {
                App.intent = intent;
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }
    }
}
