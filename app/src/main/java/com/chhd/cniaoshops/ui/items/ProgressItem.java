package com.chhd.cniaoshops.ui.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.StatusEnum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by CWQ on 2017/3/21.
 */

public class ProgressItem extends AbstractFlexibleItem<ProgressItem.Holder> {

    private StatusEnum status = StatusEnum.ON_LOAD_MORE;
    private Context context;
    private FlexibleAdapter adapter;
    private View.OnClickListener onClickListener;

    public ProgressItem() {
    }

    public ProgressItem(FlexibleAdapter adapter) {
        this.adapter = adapter;
    }

    public ProgressItem(FlexibleAdapter adapter, View.OnClickListener onClickListener) {
        this.adapter = adapter;
        this.onClickListener = onClickListener;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
        adapter.notifyItemChanged(adapter.getItemCount() - 1);

    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_load_more;
    }

    @Override
    public Holder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new Holder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, Holder holder, int position, List payloads) {

        switch (status) {
            case ON_LOAD_MORE:
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.tvFail.setVisibility(View.INVISIBLE);
                holder.tvFinish.setVisibility(View.INVISIBLE);
                break;
            case ON_EMPTY:
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.tvFail.setVisibility(View.INVISIBLE);
                holder.tvFinish.setVisibility(View.VISIBLE);
                break;
            case ON_ERROR:
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.tvFail.setVisibility(View.VISIBLE);
                holder.tvFinish.setVisibility(View.INVISIBLE);
                break;
        }
    }

    final class Holder extends FlexibleViewHolder {

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.tv_fail)
        TextView tvFail;
        @BindView(R.id.tv_finish)
        TextView tvFinish;

        public Holder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            ButterKnife.bind(this, view);

            tvFail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            onClickListener.onClick(view);
        }
    }

}
