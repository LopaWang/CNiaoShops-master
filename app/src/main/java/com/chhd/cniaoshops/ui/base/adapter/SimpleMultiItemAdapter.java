package com.chhd.cniaoshops.ui.base.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by CWQ on 2017/3/26.
 */

public abstract class SimpleMultiItemAdapter<T extends MultiItemEntity, K extends BaseViewHolder> extends SimpleAdapter<T, K> {

    protected Context context;
    private SparseArray<Integer> layouts;

    private static final int DEFAULT_VIEW_TYPE = -0xff;

    public SimpleMultiItemAdapter(RecyclerView recyclerView, List<T> data) {
        super(recyclerView, data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        Object item = mData.get(position);
        if (item instanceof MultiItemEntity) {
            return ((MultiItemEntity) item).getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    @Override
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }

}
