package com.chhd.cniaoshops.ui.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Menu;
import com.chhd.cniaoshops.global.App;
import com.chhd.cniaoshops.ui.activity.OrderActivity;
import com.chhd.cniaoshops.ui.activity.address.AddressListActivity;
import com.chhd.cniaoshops.ui.activity.UserActivity;
import com.chhd.cniaoshops.ui.base.fragment.BaseFragment;
import com.chhd.cniaoshops.ui.decoration.SpaceItemDecoration;
import com.chhd.per_library.ui.base.SimpleHolder;
import com.chhd.per_library.util.UiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by CWQ on 2016/10/24.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_mine;
    }

    private List<Menu> menus = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] titles = UiUtils.getStringArray(R.array.menu_mine_title);
        int[] icons = getIcons();
        for (int i = 0; i < titles.length; i++) {
            menus.add(new Menu(icons[i], titles[i]));
        }

        recyclerView.setAdapter(new MineAdapter(menus));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(UiUtils.dp2px(0.5f)));
    }

    private int[] getIcons() {
        TypedArray ar = getResources().obtainTypedArray(R.array.menu_mine_icon);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);

        ar.recycle();
        return resIds;
    }

    @OnClick({R.id.header})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header:
                Intent intent = new Intent(getActivity(), UserActivity.class);
                startActivity(intent, true);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.user != null) {
            if (App.user.getAvatar() != null) {
                Picasso
                        .with(getActivity())
                        .load(App.user.getAvatar().getUrl())
                        .centerCrop()
                        .resize(UiUtils.dp2px(80), UiUtils.dp2px(80))
                        .into(ivAvatar);
            }
            tvUsername.setText(App.user.getNickname());
        } else {
            ivAvatar.setImageResource(R.mipmap.ic_user);
            tvUsername.setText(R.string.click_login);
        }
    }

    class MineAdapter extends BaseQuickAdapter<Menu, BaseViewHolder> {

        public MineAdapter(List<Menu> data) {
            super(R.layout.list_item_menu, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Menu item) {
            Holder holder = new Holder(helper.itemView, helper.getAdapterPosition());
            holder.ivIcon.setImageResource(item.getIcon());
            holder.tvTitle.setText(item.getTitle());
        }

        class Holder extends SimpleHolder implements View.OnClickListener {

            @BindView(R.id.iv_icon)
            ImageView ivIcon;
            @BindView(R.id.tv_title)
            TextView tvTitle;

            private int pos;

            public Holder(View itemView, int pos) {
                super(itemView);
                this.pos = pos;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (pos) {
                    case 0: {
                        Intent intent = new Intent(getActivity(), OrderActivity.class);
                        startActivity(intent, true);
                    }
                    break;
                    case 2: {
                        Intent intent = new Intent(getActivity(), AddressListActivity.class);
                        startActivity(intent, true);
                    }
                    break;
                }
            }
        }
    }
}
