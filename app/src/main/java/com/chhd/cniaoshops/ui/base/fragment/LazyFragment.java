package com.chhd.cniaoshops.ui.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by CWQ on 2017/4/9.
 */

public abstract class LazyFragment extends BaseFragment {

    private boolean hasLazyLoad;
    private boolean hasViewCreate;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hasViewCreate && isVisibleToUser && !hasLazyLoad) {
            lazyLoad();
            hasLazyLoad = true;
        }
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hasViewCreate = true;
        if (getUserVisibleHint() && !hasLazyLoad) {
            lazyLoad();
            hasLazyLoad = true;
        }
    }


    protected abstract void lazyLoad();
}
