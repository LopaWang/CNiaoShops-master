package com.chhd.cniaoshops.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.base.fragment.BaseFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class PicFragment extends BaseFragment {

    @BindView(R.id.iv_pic)
    ImageView ivPic;

    private String imgUrl;

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_pic;
    }

    public static PicFragment newInstance(String imgUrl) {
        PicFragment fragment = new PicFragment();
        Bundle args = new Bundle();
        args.putString("imgUrl", imgUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgUrl = getArguments().getString("imgUrl");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso
                .with(getActivity())
                .load(imgUrl)
                .config(Bitmap.Config.RGB_565)
                .into(ivPic);
    }
}
