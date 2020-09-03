package com.chhd.per_library.ui.base;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

public class SimpleFmPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;

    public SimpleFmPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Bundle arguments = fragments.get(position).getArguments();
        if (arguments != null && !TextUtils.isEmpty(arguments.getString("title"))) {
            return arguments.getString("title");
        } else {
            return super.getPageTitle(position);
        }
    }
}
