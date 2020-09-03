package com.chhd.cniaoshops.biz;

import android.app.Activity;
import android.content.Context;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.per_library.util.UiUtils;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2017/3/27.
 */

public class BannerProvider implements Constant {

    private Context context;

    public BannerProvider(Context context) {
        this.context = context;

        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("context must be activity");
        }
    }

    public List<BaseSliderView> getBanner() {

        List<BaseSliderView> views = new ArrayList<>();

        String[] imgUrls = UiUtils.getStringArray(R.array.banner_img_urls);
        String[] titles = UiUtils.getStringArray(R.array.banner_titles);

        for (int i = 0; i < imgUrls.length; i++) {
            TextSliderView sliderView = new TextSliderView(context);
            sliderView.image(imgUrls[i]);
            sliderView.description(titles[i]);
            sliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderView.empty(placeholderResId);
            sliderView.error(placeholderResId);
            views.add(sliderView);
        }
        return views;
    }
}
