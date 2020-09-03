package com.chhd.cniaoshops.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.biz.BmobApi;
import com.chhd.cniaoshops.ui.base.fragment.BaseFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class RegByNameFragment extends BaseFragment {

    @BindView(R.id.et_username)
    MaterialEditText etUsername;
    @BindView(R.id.et_pwd)
    MaterialEditText etPwd;
    @BindView(R.id.et_confirm_pwd)
    MaterialEditText etConfirmPwd;

    public RegByNameFragment() {
    }

    public static RegByNameFragment newInstance(String title) {
        RegByNameFragment fragment = new RegByNameFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_reg_by_name;
    }

    @OnClick({R.id.btn_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                requestRegister();
                break;
        }
    }

    private boolean validate() {
        String username = etUsername.getText().toString();
        String pwd = etPwd.getText().toString();
        String confirmPwd = etConfirmPwd.getText().toString();
        if (TextUtils.isEmpty(username)) {
            etUsername.setError(getString(R.string.please_input_username));
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            etPwd.setError(getString(R.string.please_input_login_pwd));
            return false;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            etConfirmPwd.setError(getString(R.string.please_input_confirm_pwd));
            return false;
        }
        if (!pwd.equals(confirmPwd)) {
            etPwd.setError(getString(R.string.pwd_inconsistency));
            etConfirmPwd.setError(getString(R.string.pwd_inconsistency));
            return false;
        }
        return true;
    }

    private void requestRegister() {
        if (validate()) {
            String username = etUsername.getText().toString();
            String pwd = etPwd.getText().toString();
            new BmobApi(getActivity()).requestRegisterByName(username, pwd);
        }
    }

}
