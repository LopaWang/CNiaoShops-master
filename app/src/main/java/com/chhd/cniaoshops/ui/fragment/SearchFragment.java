package com.chhd.cniaoshops.ui.fragment;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.activity.PlaceChooseActivity;
import com.chhd.cniaoshops.ui.base.fragment.BaseFragment;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.chhd.per_library.util.SoftKeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.codetail.animation.ViewAnimationUtils;

public class SearchFragment extends BaseFragment {

    @BindView(R.id.content)
    View content;
    @BindView(R.id.search)
    View search;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.keyWord)
    AutoCompleteTextView searchText;

    private int centerX;
    private int centerY;

    private String city;

    private List<Tip> autoTips;
    private boolean isShowDropDown = false;

    public static SearchFragment newInstance(String city) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("city", city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = getArguments().getString("city");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchText.setOnItemClickListener(onItemClickListener);

        addTextChangedListener(city);

        executeCircleReveal();

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void addTextChangedListener(final String city) {
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.length() > 0) {
                    search(newText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchText.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                isShowDropDown = false;
            }
        });
    }

    private void search(String text) {
        InputtipsQuery inputquery = new InputtipsQuery(text, city);
        Inputtips inputTips = new Inputtips(getActivity(), inputquery);
        inputquery.setCityLimit(true);
        inputTips.setInputtipsListener(inputtipsListener);
        inputTips.requestInputtipsAsyn();
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (autoTips != null && autoTips.size() > position) {
                Tip tip = autoTips.get(position);
                ((PlaceChooseActivity) getActivity()).searchPoi(tip);
                onBackPressed();
            }
        }
    };

    private Inputtips.InputtipsListener inputtipsListener = new Inputtips.InputtipsListener() {

        @Override
        public void onGetInputtips(List<Tip> list, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
                autoTips = list;
                List<String> listString = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    listString.add(list.get(i).getName());
                }
                ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(getActivity(), R.layout.route_inputs, listString);
                searchText.setAdapter(aAdapter);
                aAdapter.notifyDataSetChanged();
                if (!isShowDropDown) {
                    isShowDropDown = true;
                    searchText.showDropDown();
                }
            } else {
                ToastyUtils.error("erroCode " + rCode);
            }
        }
    };

    @OnClick({R.id.iv_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                ToastyUtils.success("iv_search");
                search(searchText.getText().toString());
                break;
        }
    }

    private void executeCircleReveal() {
        search.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                search.getViewTreeObserver().removeOnPreDrawListener(this);

                search.setVisibility(View.INVISIBLE);

                centerX = ivSearch.getLeft() + ivSearch.getWidth() / 2;
                centerY = ivSearch.getTop() + ivSearch.getHeight() / 2;

                Animator reveal = ViewAnimationUtils.createCircularReveal(search, centerX, centerY, 20, hypo(search.getWidth(), search.getHeight()));
                reveal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        search.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        searchText.setFocusable(true);
                        searchText.setFocusableInTouchMode(true);
                        searchText.requestFocus();
                        SoftKeyboardUtils.showSoftInput(searchText);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                reveal.setDuration(200);
                reveal.setStartDelay(100);
                reveal.setInterpolator(new AccelerateDecelerateInterpolator());
                reveal.start();
                return true;
            }
        });
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_search;
    }

    private float hypo(int a, int b) {
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    public boolean onBackPressed() {
        Animator reveal = ViewAnimationUtils.createCircularReveal(content, centerX, centerY, hypo(content.getWidth(), content.getHeight()), 20);
        reveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                content.setVisibility(View.VISIBLE);
                hideSoftKey(searchText);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                content.setVisibility(View.INVISIBLE);
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        reveal.setDuration(200);
        reveal.setStartDelay(100);
        reveal.setInterpolator(new AccelerateDecelerateInterpolator());
        reveal.start();
        return true;
    }

    private void hideSoftKey(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
