package com.chhd.cniaoshops.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.biz.BmobApi;
import com.chhd.cniaoshops.ui.base.activity.HideSoftInputActivity;
import com.chhd.cniaoshops.util.DialogUtils;
import com.chhd.cniaoshops.util.JsonUtils;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.per_library.util.AppUtils;
import com.chhd.per_library.util.ToastUtils;
import com.chhd.sharesdk.login.LoginApi;
import com.chhd.sharesdk.login.OnLoginListener;
import com.chhd.sharesdk.login.ThirdPartyUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

public class LoginActivity extends HideSoftInputActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_account)
    MaterialEditText etAccount;
    @BindView(R.id.et_pwd)
    MaterialEditText etPwd;

    private final int REQUEST_REGISTER_ACTIVITY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        AndPermission
                .with(this)
                .requestCode(REQUEST_CODE_READ_SMS)
                .permission(Manifest.permission.READ_SMS)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        DialogUtils.newRationaleDialog(context, rationale).show();
                    }
                })
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, List<String> grantPermissions) {
                switch (requestCode) {
                    case REQUEST_CODE_READ_SMS:
                        etAccount.setText(AppUtils.getMobliePhone());
                        break;
                }
            }

            @Override
            public void onFailed(int requestCode, List<String> deniedPermissions) {
                switch (requestCode) {
                    case REQUEST_CODE_READ_SMS:
                        ToastUtils.makeText("获取权限失败");
                        if (AndPermission.hasAlwaysDeniedPermission(context, deniedPermissions)) {
                            DialogUtils.newDefineSettingDialog(context).show();
                        }
                        break;
                }
            }
        });
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.user_login);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.iv_qq})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                requestLogin();
                break;
            case R.id.tv_register: {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_REGISTER_ACTIVITY);
            }
            break;
            case R.id.iv_qq:
                Platform platform = ShareSDK.getPlatform((String) v.getTag());
                LoggerUtils.d("isAuthValid: " + platform.isAuthValid());
                if (platform.isAuthValid()) {
                    LoggerUtils.d("exportData: " + platform.getDb().exportData());
                    ThirdPartyUser user = JsonUtils.fromJson(platform.getDb().exportData(), ThirdPartyUser.class);
                    requestLogin(user);
                } else {
                    requestThirdPartyLogin((String) v.getTag());
                }
                break;
        }
    }

    /*
     * 演示执行第三方登录/注册的方法
	 *
	 * 这不是一个完整的示例代码，需要根据您项目的业务需求，改写登录/注册回调函数
	 *
	 * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
	 */
    private void requestThirdPartyLogin(String platformName) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public boolean onLogin(String platform, HashMap<String, Object> res) {
                // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
                // 此处全部给回需要注册
                ThirdPartyRegActivity.setTmpPlatform(platform);
                ThirdPartyRegActivity.setTmpRegisterListener(this);
                Intent intent = new Intent(getApplicationContext(), ThirdPartyRegActivity.class);
                startActivity(intent);
                return true;
            }

            public boolean onRegister(ThirdPartyUser user) {
                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                LoggerUtils.d("ThirdPartyUser: " + JsonUtils.toJSON(user));
                return true;
            }
        });
        api.login(this);
    }


    private void requestLogin(ThirdPartyUser user) {
        new BmobApi(this).requestLogin(user);
    }

    private boolean validate() {
        String username = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(username)) {
            etAccount.setError(getString(R.string.please_input_username));
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            etPwd.setError(getString(R.string.please_input_login_pwd));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_REGISTER_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
        }
    }

    private void requestLogin() {
        if (validate()) {
            String username = etAccount.getText().toString();
            String pwd = etPwd.getText().toString();
            new BmobApi(this).requestLogin(username, pwd);
        }
    }
}
