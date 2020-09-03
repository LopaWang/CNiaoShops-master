package com.chhd.cniaoshops.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chhd.cniaoshops.R;

/**
 * Created by CWQ on 2016/11/15.
 */

public class CnToolbar extends Toolbar {

    private TextView tvTitle;
    private TextView etSearch;
    private Button btnRight;

    public CnToolbar(Context context) {
        this(context, null);
    }

    public CnToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CnToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();

        initAttrs(attrs);
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.toolbar_cn, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        etSearch = (TextView) view.findViewById(R.id.et_search);
        btnRight = (Button) view.findViewById(R.id.btn_right);
        addView(view);
    }

    private void initAttrs(AttributeSet attrs) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.CnToolbar);

        Drawable icon = tintTypedArray.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
        if (icon != null) {
        }

        boolean isShowSearchView = tintTypedArray.getBoolean(R.styleable.CnToolbar_isShowSearchView, false);
        if (isShowSearchView) {
            showSearchView();
            hideTitleView();
            hideRightButton();
        }
    }

    public void showSearchView() {
        etSearch.setVisibility(VISIBLE);
    }

    public void hideSearchView() {
        etSearch.setVisibility(INVISIBLE);
    }

    public void showTitleView() {
        tvTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        tvTitle.setVisibility(INVISIBLE);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
        showTitleView();
    }

    public Button getRightButton() {
        return btnRight;
    }

    public void showRightButton() {
        btnRight.setVisibility(VISIBLE);
    }


    public void hideRightButton() {
        btnRight.setVisibility(INVISIBLE);
    }

}
