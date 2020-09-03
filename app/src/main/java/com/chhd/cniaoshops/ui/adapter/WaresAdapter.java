package com.chhd.cniaoshops.ui.adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.biz.CartProvider;
import com.chhd.cniaoshops.ui.activity.WaresDetailActivity;
import com.chhd.cniaoshops.ui.base.adapter.SimpleAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CWQ on 2017/3/27.
 */

public class WaresAdapter extends SimpleAdapter<Wares, BaseViewHolder> {

    public WaresAdapter(RecyclerView recyclerView, int layoutResId, List<Wares> data) {
        super(recyclerView, layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Wares item) {
        Holder holder = new Holder(helper.itemView, helper.getAdapterPosition());
        Picasso
                .with(mContext)
                .load(item.getImgUrl())
                .config(Bitmap.Config.RGB_565)
                .tag(recyclerView)
                .into(holder.ivPic);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("￥%.2f", item.getPrice()));
    }

    class Holder implements View.OnClickListener {

        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.btn_buy)
        Button btnBuy;

        private int pos;

        public Holder(View itemView, int pos) {
            ButterKnife.bind(this, itemView);
            this.pos = pos;
            btnBuy.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_buy:
                    new CartProvider().put(getData().get(pos - getHeaderLayoutCount()));
                    break;
                default:
                    Intent intent = new Intent(mContext, WaresDetailActivity.class);
                    intent.putExtra("wares", getData().get(pos));
                    mContext.startActivity(intent);
                    break;
            }
        }
    }
}
