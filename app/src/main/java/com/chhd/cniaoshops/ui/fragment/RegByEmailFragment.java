package com.chhd.cniaoshops.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.base.fragment.BaseFragment;

public class RegByEmailFragment extends BaseFragment {

    public RegByEmailFragment() {
    }

    public static RegByEmailFragment newInstance(String title) {
        RegByEmailFragment fragment = new RegByEmailFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_reg_by_email;
    }

}
