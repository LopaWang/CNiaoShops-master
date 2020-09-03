package com.chhd.per_library.ui.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class SimpleAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> data;
    protected LayoutInflater inflater;

    public SimpleAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void notifyDataChanged(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

}
