package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.R;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView {

    private View v;
    private ImageView target;
    private TextView description;

    public TextSliderView(Context context) {
        super(context);

        v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text, null);
        target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        description = (TextView) v.findViewById(R.id.description);
    }

    @Override
    public View getView() {
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
