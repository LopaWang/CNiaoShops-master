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

public class LoginByNorFragment extends BaseFragment {

    public LoginByNorFragment() {
    }

    public static LoginByNorFragment newInstance() {
        LoginByNorFragment fragment = new LoginByNorFragment();
        return fragment;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_login_by_nor;
    }
}
