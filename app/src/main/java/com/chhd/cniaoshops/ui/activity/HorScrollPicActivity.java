package com.chhd.cniaoshops.ui.activity;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.base.activity.SwipeBackActivity;
import com.chhd.cniaoshops.ui.fragment.PicFragment;
import com.chhd.cniaoshops.ui.listener.PageChangeListener;
import com.chhd.per_library.ui.base.SimpleFmPagerAdapter;
import com.liuguangqiang.progressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HorScrollPicActivity extends SwipeBackActivity {

    @BindView(R.id.vp_pic)
    ViewPager vpPic;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.circle_progress_bar)
    CircleProgressBar progressBar;
    @BindView(R.id.tv_page)
    TextView tvPage;

    private List<Fragment> fragments = new ArrayList<>();
    private String[] imgUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgUrls = getIntent().getStringArrayExtra("imgUrls");
        final int pos = getIntent().getIntExtra("pos", 0);

        for (int i = 0; i < imgUrls.length; i++) {
            String imgUrl = imgUrls[i];
            Fragment fragment = PicFragment.newInstance(imgUrl);
            fragments.add(fragment);
        }
        vpPic.setAdapter(new SimpleFmPagerAdapter(getSupportFragmentManager(), fragments));
        vpPic.setCurrentItem(pos);
        vpPic.addOnPageChangeListener(onPageChangeListener);

        tvPage.setText(String.format("%1$s / %2$s", pos + 1, imgUrls.length));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Picasso
                    .with(this)
                    .load(imgUrls[pos])
                    .config(Bitmap.Config.RGB_565)
                    .into(ivPic);
            ivPic.setVisibility(View.VISIBLE);
            Transition transition = getWindow().getSharedElementEnterTransition();
            transition.addListener(new Transition.TransitionListener() {

                @Override
                public void onTransitionStart(Transition transition) {
                    vpPic.setVisibility(View.GONE);
                    executeCircleReveal();
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            vpPic.setVisibility(View.VISIBLE);
                            ivPic.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        } else {
            ivPic.setVisibility(View.GONE);
        }

        setEnableSwipe(false);
    }

    private void executeCircleReveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final float finalRadius = (float) Math.hypot(getRootView().getWidth(), getRootView().getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(
                    getRootView(),
                    (ivPic.getLeft() + ivPic.getRight()) / 2,
                    (ivPic.getTop() + ivPic.getBottom()) / 2,
                    (float) ivPic.getWidth() / 2,
                    finalRadius
            );
            anim.setDuration(1000);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.start();
        }
    }

    public static PageChangeListener pageChangeListener;

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Picasso
                    .with(context)
                    .load(imgUrls[position])
                    .config(Bitmap.Config.RGB_565)
                    .into(ivPic);
            tvPage.setText(String.format("%1$s / %2$s", position + 1, imgUrls.length));
            if (pageChangeListener != null) {
                pageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageChangeListener = null;
    }

    public static void setPageChangeListener(PageChangeListener pageChangeListener) {
        HorScrollPicActivity.pageChangeListener = pageChangeListener;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_hor_scroll_pic;
    }

    @Override
    public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
        super.onViewPositionChanged(fractionAnchor, fractionScreen);
        progressBar.setProgress((int) (progressBar.getMax() * fractionAnchor));
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                exit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ivPic.setVisibility(View.VISIBLE);
//            finishAfterTransition();
            finish();
            overridePendingTransition(R.anim.set_aty_exit_aty_enter, R.anim.set_aty_exit_aty_exit);
        } else {
            finish();
            overridePendingTransition(R.anim.set_aty_exit_aty_enter, R.anim.set_aty_exit_aty_exit);
        }
    }

    @Override
    protected int getStatusBarColorResId() {
        return android.R.color.transparent;
    }
}
