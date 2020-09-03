package com.chhd.cniaoshops.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Banner;
import com.chhd.cniaoshops.bean.Campaign;
import com.chhd.cniaoshops.bean.HomeCampaign;
import com.chhd.cniaoshops.bean.HomeCategory;
import com.chhd.cniaoshops.biz.BannerProvider;
import com.chhd.cniaoshops.http.OnResponse;
import com.chhd.cniaoshops.ui.activity.WaresListActivity;
import com.chhd.cniaoshops.ui.adapter.HomeCategoryAdapter;
import com.chhd.cniaoshops.ui.base.fragment.BaseFragment;
import com.chhd.cniaoshops.ui.decoration.SpaceItemDecoration;
import com.chhd.cniaoshops.ui.listener.clazz.SliderClickListener;
import com.chhd.per_library.util.UiUtils;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CWQ on 2016/10/24.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private final int REQUEST_FORM_HOR_SCROLL_PIC_ACTIVITY = 100;

    private View header;
    private List<HomeCategory> data = new ArrayList<>();
    private List<Banner> banners = new ArrayList<>();
    private List<HomeCampaign> campaigns = new ArrayList<>();
    private HomeCategoryAdapter adapter;
    private View empty;
    private SliderLayout sliderLayout;


    @Override
    public int getLayoutResID() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        refresh();
    }

    private void requestHomeCampaign() {

        String url = SERVER_URL + "campaign/recommend";

        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);

        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, request, new OnResponse<String>() {

            @Override
            public void succeed(int what, Response<String> response) {
                Type type = new TypeToken<List<HomeCampaign>>() {
                }.getType();
                List<HomeCampaign> list = new Gson().fromJson(response.get(), type);
                showHomeCampaign(list);
                adapter.setCustomEmptyView();
            }

            @Override
            public void failed(int what, Response<String> response) {
                super.failed(what, response);
                adapter.setCustomEmptyView(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                });
            }

            @Override
            public void after(int what) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void showHomeCampaign(List<HomeCampaign> homeCampaigns) {
        for (int i = 0; i < homeCampaigns.size(); i++) {
            if (i % 2 == 0) {
                homeCampaigns.get(i).setItemType(HomeCategory.TYPE_RIGHT);
            } else {
                homeCampaigns.get(i).setItemType(HomeCategory.TYPE_LEFT);
            }
        }
        campaigns.clear();
        campaigns.addAll(homeCampaigns);
        adapter.notifyDataSetChanged();
    }

    private void initView() {

        initSliderLayout();

        initRecyclerView();

        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(SWIPE_REFRESH_LAYOUT_COLORS);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    private void refresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
    }

    private void initRecyclerView() {
        adapter = new HomeCategoryAdapter(recyclerView, campaigns);
        adapter.addHeaderView(header);
        adapter.setOnItemChildClickListener(onItemChildClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(UiUtils.dp2px(DIMEN_NORMAL)));
    }


    private BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            rotation(view, position);
            return false;
        }
    };

    private void rotation(final View v, final int position) {
        v.setEnabled(false);
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationY", 0.0F, 180.0F).setDuration(150);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(v, "rotationY", 180.0F, 0.0F).setDuration(150);
                animator1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        HomeCampaign campaign = campaigns.get(position);
                        switch (v.getId()) {
                            case R.id.iv_big:
                                campaignClick(campaign.getCpOne());
                                break;
                            case R.id.iv_small_top:
                                campaignClick(campaign.getCpTwo());
                                break;
                            case R.id.iv_small_bottom:
                                campaignClick(campaign.getCpThree());
                                break;
                        }
                        v.setEnabled(true);
                    }
                });
                animator1.start();
            }
        });
        animator.start();
    }

    private void campaignClick(Campaign campaign) {
        Intent intent = new Intent(getActivity(), WaresListActivity.class);
        intent.putExtra("campaignId", campaign.getId());
        startActivity(intent);
    }

    private void initHomeCategory() {

        data.add(new HomeCategory("热门活动", R.mipmap.img_big_1, R.mipmap.img_1_small1, R.mipmap.img_1_small2));
        data.add(new HomeCategory("有利可图", R.mipmap.img_big_4, R.mipmap.img_4_small1, R.mipmap.img_4_small2));
        data.add(new HomeCategory("品牌街", R.mipmap.img_big_2, R.mipmap.img_2_small1, R.mipmap.img_2_small2));
        data.add(new HomeCategory("金融街 包赚翻", R.mipmap.img_big_1, R.mipmap.img_3_small1, R.mipmap.imag_3_small2));
        data.add(new HomeCategory("超值购", R.mipmap.img_big_0, R.mipmap.img_0_small1, R.mipmap.img_0_small2));

        for (int i = 0; i < data.size(); i++) {
            if (i % 2 == 0) {
                data.get(i).setItemType(HomeCategory.TYPE_LEFT);
            } else {
                data.get(i).setItemType(HomeCategory.TYPE_RIGHT);
            }
        }

    }

    private void initSliderLayout() {

        header = View.inflate(getActivity(), R.layout.header_banner, null);
        sliderLayout = ButterKnife.findById(header, R.id.slider_layout);

        View indicators = View.inflate(getActivity(), R.layout.indicators_bird, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, UiUtils.dp2px(BANNER_DESCRIPTION_LAYOUT_HEIGHT));
        params.alignWithParent = true;
        params.rightMargin = UiUtils.dp2px(10);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((RelativeLayout) header).addView(indicators, params);
        PagerIndicator pagerIndicator = ButterKnife.findById(header, R.id.pager_indicator);
        sliderLayout.setCustomIndicator(pagerIndicator);

        sliderLayout.startAutoCycle(5000, 5000, true);

        List<BaseSliderView> banners = new BannerProvider(getActivity()).getBanner();
        for (BaseSliderView bannser : banners) {
            bannser.setOnSliderClickListener(new SliderClickListener(getActivity(), sliderLayout));
            sliderLayout.addSlider(bannser);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sliderLayout != null) {
            sliderLayout.stopAutoCycle();
        }
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            requestHomeCampaign();
        }
    };
}
