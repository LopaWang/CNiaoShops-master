package com.chhd.cniaoshops.ui.base.lvan;

import android.content.Context;

import java.util.List;

/**
 * 菜鸟窝lvan
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {

    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }

}
