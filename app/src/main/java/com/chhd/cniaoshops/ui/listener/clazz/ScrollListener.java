package com.chhd.cniaoshops.ui.listener.clazz;

import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

/**
 * Created by CWQ on 2017/4/12.
 */

public class ScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            Picasso.with(recyclerView.getContext()).resumeTag(recyclerView);
        } else {
            Picasso.with(recyclerView.getContext()).pauseTag(recyclerView);
        }
    }
}
