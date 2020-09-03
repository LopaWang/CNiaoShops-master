package com.chhd.cniaoshops.ui.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by CWQ on 2017/4/11.
 */

public class StaggeredSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public StaggeredSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(space, space, space, space);
    }
}
