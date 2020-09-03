package com.chhd.cniaoshops.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chhd.cniaoshops.R;

/**
 * Created by CWQ on 2017/3/30.
 */

public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    private TextView tvNum;
    private Button btnSub;
    private Button btnAdd;

    private OnButtonClickListener onButtonClickListener;

    private int maxValue = 999;
    private int minValue = 1;
    private int value;
    private Vibrator vibrator;

    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        LayoutInflater.from(context).inflate(R.layout.widget_number_add_sub, this, true);

        tvNum = (TextView) findViewById(R.id.tv_num);
        btnSub = (Button) findViewById(R.id.btn_sub);
        btnAdd = (Button) findViewById(R.id.btn_add);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberAddSubView, defStyleAttr, 0);
            value = array.getInt(R.styleable.NumberAddSubView_value, 1);
            minValue = array.getInt(R.styleable.NumberAddSubView_min_value, 1);
            maxValue = array.getInt(R.styleable.NumberAddSubView_max_value, 999);
        }

        btnSub.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View v) {
        vibrator.vibrate(15);
        switch (v.getId()) {
            case R.id.btn_sub:
                numSub();
                if (onButtonClickListener != null) {
                    onButtonClickListener.onButtonSubClick(v, value);
                }
                break;
            case R.id.btn_add:
                numAdd();
                if (onButtonClickListener != null) {
                    onButtonClickListener.onButtonAddClick(v, value);
                }
                break;
        }

    }

    private void numAdd() {
        if (value < maxValue)
            value = value + 1;
        tvNum.setText(value + "");
    }

    private void numSub() {
        if (value > minValue)
            value = value - 1;
        tvNum.setText(value + "");
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tvNum.setText(value + "");
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public interface OnButtonClickListener {

        void onButtonAddClick(View view, int value);

        void onButtonSubClick(View view, int value);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

}
