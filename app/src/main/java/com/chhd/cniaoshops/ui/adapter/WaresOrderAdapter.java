package com.chhd.cniaoshops.ui.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.ShoppingCart;
import com.chhd.cniaoshops.ui.base.adapter.SimpleAdapter;
import com.chhd.per_library.ui.base.SimpleHolder;
import com.chhd.per_library.util.UiUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;

/**
 * Created by CWQ on 2017/4/23.
 */

public class WaresOrderAdapter extends SimpleAdapter<ShoppingCart, BaseViewHolder> {


    public WaresOrderAdapter(List<ShoppingCart> data) {
        super(R.layout.list_item_wares_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShoppingCart item) {
        Holder holder = new Holder(helper.itemView);
        Picasso
                .with(mContext)
                .load(item.getImgUrl())
                .centerCrop()
                .resize(UiUtils.dp2px(60), UiUtils.dp2px(60))
                .config(Bitmap.Config.RGB_565)
                .into(holder.ivPic);
    }

    class Holder extends SimpleHolder {

        @BindView(R.id.iv_pic)
        ImageView ivPic;

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
