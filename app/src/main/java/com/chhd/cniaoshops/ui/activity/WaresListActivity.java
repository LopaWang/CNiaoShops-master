package com.chhd.cniaoshops.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.base.activity.BaseActivity;
import com.chhd.cniaoshops.ui.fragment.WaresListFragment;
import com.chhd.per_library.ui.base.SimpleFmPagerAdapter;
import com.chhd.per_library.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WaresListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{UiUtils.getString(R.string.defualt), UiUtils.getString(R.string.sales_volume), UiUtils.getString(R.string.price)};
    private int[] orderBys = new int[]{0, 1, 2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        long campaignId = getIntent().getLongExtra("campaignId", 0);

        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
            Fragment fragment = WaresListFragment.newInstance(titles[i], campaignId, orderBys[i]);
            fragments.add(fragment);
        }
        viewPager.setAdapter(new SimpleFmPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(titles.length - 1);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.wares_list);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_wares_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, MENU_DEFAULT_ID, 0, R.string.list);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_dehaze_white_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DEFAULT_ID:
                if (getString(R.string.grid).equals(item.getTitle())) {
                    item.setTitle(R.string.list);
                    item.setIcon(R.drawable.ic_dehaze_white_24dp);
                    for (Fragment fragment : fragments) {
                        ((WaresListFragment) fragment).setGridList();
                    }
                } else {
                    item.setTitle(R.string.grid);
                    item.setIcon(R.drawable.ic_grid_on_white_24dp);
                    for (Fragment fragment : fragments) {
                        ((WaresListFragment) fragment).setVerList();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
