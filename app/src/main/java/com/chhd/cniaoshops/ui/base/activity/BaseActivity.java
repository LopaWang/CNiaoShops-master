package com.chhd.cniaoshops.ui.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.global.App;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.ui.activity.LoginActivity;
import com.chhd.per_library.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements Constant {

    protected final int MENU_DEFAULT_ID = 10;
    protected Activity context;

    public static List<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());

        ButterKnife.bind(this);

        context = this;

        activities.add(this);

        setStatusBarColor(getStatusBarColor());
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LottieAnimationView emptyAnimView = (LottieAnimationView) findViewById(R.id.empty_animation_view);
        if (emptyAnimView != null && emptyAnimView.isAnimating()) {
            emptyAnimView.cancelAnimation();
        }
        activities.remove(this);
    }

    public abstract int getLayoutResID();

    protected int getStatusBarColorResId() {
        return R.color.colorPrimaryDark;
    }

    protected int getStatusBarColor() {
        return UiUtils.getColor(getStatusBarColorResId());
    }

    protected View getRootView() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LottieAnimationView emptyAnimView = (LottieAnimationView) findViewById(R.id.empty_animation_view);
        if (emptyAnimView != null && !emptyAnimView.isAnimating()) {
            emptyAnimView.resumeAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LottieAnimationView emptyAnimView = (LottieAnimationView) findViewById(R.id.empty_animation_view);
        if (emptyAnimView != null && emptyAnimView.isAnimating()) {
            emptyAnimView.pauseAnimation();
        }
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    protected void startActivity(Intent intent, boolean isRequireLogin) {
        if (isRequireLogin) {
            if (App.user != null) {
                super.startActivity(intent);
            } else {
                App.intent = intent;
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }
    }
}
