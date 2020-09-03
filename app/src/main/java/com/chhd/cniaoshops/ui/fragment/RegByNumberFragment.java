package com.chhd.cniaoshops.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.biz.BmobApi;
import com.chhd.cniaoshops.http.bmob.BmobEx;
import com.chhd.cniaoshops.ui.base.fragment.BaseFragment;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.per_library.util.AppUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class RegByNumberFragment extends BaseFragment {

    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.et_code)
    MaterialEditText etCode;
    @BindView(R.id.et_phone)
    MaterialEditText etPhone;
    @BindView(R.id.et_pwd)
    MaterialEditText etPwd;

    public RegByNumberFragment() {
    }

    public static RegByNumberFragment newInstance(String title) {
        RegByNumberFragment fragment = new RegByNumberFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_reg_by_number;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etPhone.setText(AppUtils.getMobliePhone());
    }

    @OnClick({R.id.btn_code, R.id.btn_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_code:
                sendCode();
                break;
            case R.id.btn_register:
                requestRegister(etPhone.getText().toString(), etCode.getText().toString(), etPwd.getText().toString());
                break;
        }
    }

    private boolean validate(boolean isPhone) {
        String phone = etPhone.getText().toString();
        String code = etCode.getText().toString();
        String pwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError(getString(R.string.please_input_moblie_phone));
            return false;
        }
        if (TextUtils.isEmpty(code) && !isPhone) {
            etCode.setError(getString(R.string.please_input_code));
            return false;
        }
        if (TextUtils.isEmpty(pwd) && !isPhone) {
            etPwd.setError(getString(R.string.please_input_login_pwd));
            return false;
        }
        return true;
    }

    private void requestRegister(String phone, String code, String pwd) {
        new BmobApi(getActivity()).requestRegisterByNumber(phone, code, pwd);
    }

    private void sendCode() {
        if (validate(true)) {
            final int count = 60;
            Observable
                    .interval(0, 1000, TimeUnit.MILLISECONDS)
                    .take(count)
                    .map(new Func1<Long, Long>() {
                        @Override
                        public Long call(Long aLong) {
                            return count - aLong - 1;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            btnCode.setEnabled(false);
                        }
                    })
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {
                            btnCode.setEnabled(true);
                            btnCode.setText(R.string.send_code);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Long aLong) {
                            btnCode.setText("" + aLong);
                        }
                    });
            requestSendCode(etPhone.getText().toString());
        }
    }

    private void requestSendCode(String phone) {
        BmobSMS.requestSMSCode(phone, "菜鸟商店", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    LoggerUtils.i("smsId: " + smsId);
                } else {
                    BmobEx.handlerError(e);
                }
            }
        });
    }
}
