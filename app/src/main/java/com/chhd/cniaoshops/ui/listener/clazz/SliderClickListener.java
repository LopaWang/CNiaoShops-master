package com.chhd.cniaoshops.ui.listener.clazz;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.activity.HorScrollPicActivity;
import com.chhd.cniaoshops.ui.listener.PageChangeListener;
import com.chhd.per_library.util.UiUtils;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by CWQ on 2017/4/8.
 */

public class SliderClickListener implements BaseSliderView.OnSliderClickListener {

    private Activity activity;
    private SliderLayout sliderLayout;

    public SliderClickListener(Activity activity, SliderLayout sliderLayout) {
        this.activity = activity;
        this.sliderLayout = sliderLayout;
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(activity, HorScrollPicActivity.class);
        intent.putExtra("imgUrls", UiUtils.getStringArray(R.array.banner_img_urls));
        intent.putExtra("pos", sliderLayout.getCurrentPosition());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            HorScrollPicActivity.setPageChangeListener(pageChangeListener);
            sliderLayout.setTransitionName("img");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, sliderLayout, sliderLayout.getTransitionName());
            activity.startActivityForResult(intent, 0, options.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.set_aty_enter_aty_enter, R.anim.set_aty_enter_aty_exit);
        }
    }

    private PageChangeListener pageChangeListener = new PageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            sliderLayout.setCurrentPosition(position);
        }
    };
}
