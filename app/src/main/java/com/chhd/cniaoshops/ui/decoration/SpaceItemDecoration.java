package com.chhd.cniaoshops.ui.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Andy on 2016/11/7.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private int space;
    private int orientation = VERTICAL;

    private int left;
    private int top;
    private int right;
    private int bottom;

    private boolean isEquidistance;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int space, int orientation) {
        this.space = space;
        this.orientation = orientation;
    }

    public SpaceItemDecoration(int space, int orientation, boolean isEquidistance) {
        this.space = space;
        this.orientation = orientation;
        this.isEquidistance = isEquidistance;

        left = space;
        top = space;
        right = space;
        bottom = space;
    }

    public SpaceItemDecoration(int space, int orientation, int left, int top, int right, int bottom) {
        this.space = space;
        this.orientation = orientation;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.set(left, top, right, 0);
            } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                outRect.set(left, space, right, bottom);
            } else {
                outRect.set(left, space, right, 0);
            }
        } else {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.set(left, top, 0, bottom);
            } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                outRect.set(space, top, right, bottom);
            } else {
                outRect.set(space, top, 0, bottom);
            }
        }
    }

}
