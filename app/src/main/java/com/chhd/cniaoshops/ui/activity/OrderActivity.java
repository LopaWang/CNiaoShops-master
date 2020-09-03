package com.chhd.cniaoshops.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Order;
import com.chhd.cniaoshops.http.bmob.SimpleFindListener;
import com.chhd.cniaoshops.ui.base.activity.BaseActivity;
import com.chhd.cniaoshops.ui.fragment.OrderFragment;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.per_library.ui.base.SimpleFmPagerAdapter;
import com.chhd.per_library.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

public class OrderActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{UiUtils.getString(R.string.whole), UiUtils.getString(R.string.unpaid), UiUtils.getString(R.string.pay_success), UiUtils.getString(R.string.pay_fail)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        requestOrder();
    }

    private void requestOrder() {
        BmobQuery<Order> query = new BmobQuery<>();
        query.addWhereEqualTo("user", BmobUser.getCurrentUser());
        query.findObjects(new SimpleFindListener<Order>(this) {
            @Override
            public void success(List<Order> list) {
                showData(list);
            }
        });
    }

    private void showData(List<Order> list) {
        LoggerUtils.i("size: " + list.size());
        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
            Fragment fragment = OrderFragment.newInstance(titles[i], list);
            fragments.add(fragment);
        }
        viewPager.setAdapter(new SimpleFmPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(titles.length - 1);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_order);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_order;
    }
}
