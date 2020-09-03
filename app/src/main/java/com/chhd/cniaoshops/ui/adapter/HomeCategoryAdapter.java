package com.chhd.cniaoshops.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.HomeCampaign;
import com.chhd.cniaoshops.bean.HomeCategory;
import com.chhd.cniaoshops.ui.base.adapter.SimpleMultiItemAdapter;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by CWQ on 2016/11/16.
 */

public class HomeCategoryAdapter extends SimpleMultiItemAdapter<HomeCampaign, BaseViewHolder> {

    public HomeCategoryAdapter(RecyclerView recyclerView, List<HomeCampaign> data) {
        super(recyclerView, data);
        addItemType(HomeCategory.TYPE_RIGHT, R.layout.list_item_home_right);
        addItemType(HomeCategory.TYPE_LEFT, R.layout.list_item_home_left);
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeCampaign campaign) {
        try {
            ImageView ivBig = holder.getView(R.id.iv_big);
            ImageView ivSmallTop = holder.getView(R.id.iv_small_top);
            ImageView ivSmallBottom = holder.getView(R.id.iv_small_bottom);

            holder.setText(R.id.tv_title, campaign.getTitle());
            holder.setVisible(R.id.space, holder.getAdapterPosition() == getItemCount() - 1);

            Picasso
                    .with(mContext)
                    .load(campaign.getCpOne().getImgUrl())
                    .config(Bitmap.Config.RGB_565)
                    .tag(recyclerView)
                    .into(ivBig);

            Picasso
                    .with(mContext)
                    .load(campaign.getCpTwo().getImgUrl())
                    .config(Bitmap.Config.RGB_565)
                    .tag(recyclerView)
                    .into(ivSmallTop);

            Picasso
                    .with(mContext)
                    .load(campaign.getCpThree().getImgUrl())
                    .config(Bitmap.Config.RGB_565)
                    .tag(recyclerView)
                    .into(ivSmallBottom);

            holder.addOnClickListener(R.id.iv_big);
            holder.addOnClickListener(R.id.iv_small_top);
            holder.addOnClickListener(R.id.iv_small_bottom);

        } catch (Exception e) {
            LoggerUtils.e(e);
        }

    }

}
