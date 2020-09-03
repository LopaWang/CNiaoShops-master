package com.chhd.cniaoshops.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Order;
import com.chhd.cniaoshops.bean.ShoppingCart;
import com.chhd.cniaoshops.ui.base.adapter.SimpleAdapter;
import com.chhd.per_library.ui.base.SimpleHolder;
import com.chhd.per_library.util.UiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by CWQ on 2017/5/1.
 */

public class OrderAdapter extends SimpleAdapter<Order, BaseViewHolder> {

    public OrderAdapter(RecyclerView recyclerView, List<Order> data) {
        super(recyclerView, R.layout.list_item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        Holder holder = new Holder(helper.itemView);
        holder.tvNum.setText(String.format("订单编号：%d", item.getOrderNum()));

        int status = item.getStatus();
        switch (status) {
            case Order.STATUS_PAY_WAIT:
                holder.tvStatus.setText(mContext.getString(R.string.unpaid));
                holder.tvStatus.setTextColor(UiUtils.getColor(android.R.color.holo_orange_light));
                holder.action0.setVisibility(View.VISIBLE);
                holder.action1.setVisibility(View.GONE);
                break;
            case Order.STATUS_PAY_SUCCESS:
                holder.tvStatus.setText(mContext.getString(R.string.pay_success));
                holder.tvStatus.setTextColor(UiUtils.getColor(android.R.color.holo_green_light));
                holder.action0.setVisibility(View.GONE);
                holder.action1.setVisibility(View.VISIBLE);
                break;
            case Order.STATUS_PAY_FAIL:
                holder.tvStatus.setText(mContext.getString(R.string.pay_fail));
                holder.tvStatus.setTextColor(UiUtils.getColor(android.R.color.holo_red_light));
                holder.action0.setVisibility(View.VISIBLE);
                holder.action1.setVisibility(View.GONE);
                break;
        }
        // <font color='#999999'>￥%.2f</font>
        holder.tvPrice.setText(Html.fromHtml(String.format("共计%d件商品，合计：<font color='#ff00000'>￥%.2f</font>", item.getCarts().size(), item.getAmount())));

        List<String> urls = new ArrayList<>();
        for (ShoppingCart cart : item.getCarts()) {
            urls.add(cart.getImgUrl());
        }
        holder.gridView.setAdapter(new ImageAdapter(mContext, urls));
    }

    class Holder extends SimpleHolder {

        @BindView(R.id.tv_number)
        TextView tvNum;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.action0)
        View action0;
        @BindView(R.id.action1)
        View action1;
        @BindView(R.id.grid_view)
        GridView gridView;

        public Holder(View itemView) {
            super(itemView);
        }
    }

    class ImageAdapter extends com.chhd.per_library.ui.base.SimpleAdapter<String> {

        public ImageAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.grid_item_order_wares, parent, false);
            Holder holder = new Holder(convertView);
            Picasso
                    .with(context)
                    .load(getItem(position))
                    .into(holder.ivPic);
            return convertView;
        }

        class Holder extends SimpleHolder {

            @BindView(R.id.iv_pic)
            ImageView ivPic;

            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
