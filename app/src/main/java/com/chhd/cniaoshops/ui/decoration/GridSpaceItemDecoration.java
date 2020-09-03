package com.chhd.cniaoshops.ui.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Andy on 2017/1/20.
 */

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int space;
    private boolean includeEdge = false;
    private boolean isContainHeader = false;

    public GridSpaceItemDecoration(int spanCount, int space) {
        this.spanCount = spanCount;
        this.space = space;
    }

    public GridSpaceItemDecoration(int spanCount, int space, boolean includeEdge) {
        this.spanCount = spanCount;
        this.space = space;
        this.includeEdge = includeEdge;
    }

    public GridSpaceItemDecoration(int spanCount, int space, boolean includeEdge, boolean isContainHeader) {
        this.spanCount = spanCount;
        this.space = space;
        this.includeEdge = includeEdge;
        this.isContainHeader = isContainHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (isContainHeader) {
            int position = parent.getChildAdapterPosition(view); // item position
            if (position == 0) {

            } else {
                int column = (position - 1) % spanCount; // item column

                if (includeEdge) {
                    outRect.left = space - column * space / spanCount; // space - column * ((1f / spanCount) * space)
                    outRect.right = (column + 1) * space / spanCount; // (column + 1) * ((1f / spanCount) * space)

                    if (position - 1 < spanCount) { // top edge
                        outRect.top = space;
                    }

                    outRect.bottom = space; // item bottom
                } else {
                    outRect.left = column * space / spanCount; // column * ((1f / spanCount) * space)
                    outRect.right = space - (column + 1) * space / spanCount; // space - (column + 1) * ((1f /    spanCount) * space)

                    if (position - 1 >= spanCount) {
                        outRect.top = space; // item top
                    }
                }
            }
        } else {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column
            if (includeEdge) {
                outRect.left = space - column * space / spanCount; // space - column * ((1f / spanCount) * space)
                outRect.right = (column + 1) * space / spanCount; // (column + 1) * ((1f / spanCount) * space)

                if (position < spanCount) { // top edge
                    outRect.top = space;
                }

                outRect.bottom = space; // item bottom
            } else {
                outRect.left = column * space / spanCount; // column * ((1f / spanCount) * space)
                outRect.right = space - (column + 1) * space / spanCount; // space - (column + 1) * ((1f /    spanCount) * space)

                if (position >= spanCount) {
                    outRect.top = space; // item top
                }
            }
        }
    }
}
