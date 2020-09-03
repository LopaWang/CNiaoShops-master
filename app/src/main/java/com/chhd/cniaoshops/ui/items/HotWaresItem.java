package com.chhd.cniaoshops.ui.items;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.biz.CartProvider;
import com.chhd.cniaoshops.ui.activity.WaresDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by CWQ on 2017/3/15.
 */

public class HotWaresItem extends AbstractFlexibleItem<HotWaresItem.Holder> implements View.OnClickListener {

    private Wares wares;
    private RecyclerView recyclerView;


    public HotWaresItem(Wares wares, RecyclerView recyclerView) {
        this.wares = wares;
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.list_item_wares;
    }

    @Override
    public Holder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new Holder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }


    @Override
    public void bindViewHolder(FlexibleAdapter adapter, Holder holder, int position, List payloads) {
        Picasso
                .with(recyclerView.getContext())
                .load(wares.getImgUrl())
                .config(Bitmap.Config.RGB_565)
                .tag(recyclerView)
                .into(holder.ivPic);
        holder.tvName.setText(wares.getName());
        holder.tvPrice.setText("" + wares.getPrice());
        holder.btnBuy.setOnClickListener(this);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy:
                new CartProvider().put(wares);
                break;
            default:
                Intent intent = new Intent(v.getContext(), WaresDetailActivity.class);
                intent.putExtra("wares", wares);
                v.getContext().startActivity(intent);
                break;
        }
    }

    class Holder extends FlexibleViewHolder {

        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.btn_buy)
        Button btnBuy;

        public Holder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
