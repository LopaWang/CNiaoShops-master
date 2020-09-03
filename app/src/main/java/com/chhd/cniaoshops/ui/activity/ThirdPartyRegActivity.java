package com.chhd.cniaoshops.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.sharesdk.login.ThirdPartyUser;
import com.chhd.cniaoshops.ui.base.activity.BaseActivity;
import com.chhd.cniaoshops.util.JsonUtils;
import com.chhd.sharesdk.login.OnLoginListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

public class ThirdPartyRegActivity extends BaseActivity {

    private static OnLoginListener tmpRegisterListener;
    private static String tmpPlatform;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_avatar)
    ImageView ivAvater;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    private ThirdPartyUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        Platform platform = ShareSDK.getPlatform(tmpPlatform);
        user = JsonUtils.fromJson(platform.getDb().exportData(), ThirdPartyUser.class);

        Picasso
                .with(this)
                .load(user.getIcon())
                .into(ivAvater);

        tvNickname.setText(user.getNickname());
    }

    public static void setTmpPlatform(String tmpPlatform) {
        ThirdPartyRegActivity.tmpPlatform = tmpPlatform;
    }

    public static void setTmpRegisterListener(OnLoginListener tmpRegisterListener) {
        ThirdPartyRegActivity.tmpRegisterListener = tmpRegisterListener;
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.user_information);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_third_party_reg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, MENU_DEFAULT_ID, 0, R.string.register);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DEFAULT_ID:
                tmpRegisterListener.onRegister(user);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
