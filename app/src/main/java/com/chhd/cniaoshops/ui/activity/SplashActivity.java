package com.chhd.cniaoshops.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.base.activity.BaseActivity;
import com.chhd.cniaoshops.global.App;
import com.chhd.cniaoshops.util.LoggerUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.animation_view_0)
    LottieAnimationView emojiView;
    @BindViews({
            R.id.animation_view_1, R.id.animation_view_2, R.id.animation_view_3,
            R.id.animation_view_4, R.id.animation_view_5, R.id.animation_view_6,
            R.id.animation_view_7
    })
    List<LottieAnimationView> lotterLetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (App.isHotRun) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            init();
        }
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    public void onBackPressed() {
    }

    private void init() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    for (final LottieAnimationView letter : lotterLetters) {
                        sleep(300);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                letter.playAnimation();
                                if (lotterLetters.indexOf(letter) == lotterLetters.size() - 1) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 1000);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    LoggerUtils.e(e);
                }
            }
        }.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        emojiView.cancelAnimation();
        for (LottieAnimationView letter : lotterLetters) {
            letter.cancelAnimation();
        }
    }
}
