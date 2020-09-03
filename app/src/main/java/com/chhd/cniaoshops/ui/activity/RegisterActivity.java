package com.chhd.cniaoshops.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.base.activity.HideSoftInputActivity;
import com.chhd.cniaoshops.ui.fragment.RegByEmailFragment;
import com.chhd.cniaoshops.ui.fragment.RegByNameFragment;
import com.chhd.cniaoshops.ui.fragment.RegByNumberFragment;
import com.chhd.per_library.ui.base.SimpleFmPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RegisterActivity extends HideSoftInputActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        fragments.add(RegByNameFragment.newInstance(getString(R.string.username)));
        fragments.add(RegByNumberFragment.newInstance(getString(R.string.mobile_number)));
        fragments.add(RegByEmailFragment.newInstance(getString(R.string.email)));

        for (int i = 0; i < fragments.size(); i++) {
            Fragment fm = fragments.get(i);
            tabLayout.addTab(tabLayout.newTab().setText(fm.getArguments().getString("title")));
        }

        viewPager.setAdapter(new SimpleFmPagerAdapter(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.user_register);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_register;
    }

}
