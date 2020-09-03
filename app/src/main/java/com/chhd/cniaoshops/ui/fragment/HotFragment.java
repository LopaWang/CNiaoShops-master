package com.chhd.cniaoshops.ui.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Page;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.http.SimpleCallback;
import com.chhd.cniaoshops.ui.StatusEnum;
import com.chhd.cniaoshops.ui.base.fragment.BaseFragment;
import com.chhd.cniaoshops.ui.decoration.SpaceItemDecoration;
import com.chhd.cniaoshops.ui.adapter.HotWaresAdapter;
import com.chhd.cniaoshops.ui.items.HotWaresItem;
import com.chhd.cniaoshops.ui.items.ProgressItem;
import com.chhd.cniaoshops.ui.widget.EmptyView;
import com.chhd.cniaoshops.ui.widget.header.SinaRefreshView;
import com.chhd.per_library.util.UiUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lzy.okgo.OkGo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import eu.davidea.fastscroller.FastScroller;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import okhttp3.Call;

/**
 * Created by CWQ on 2016/10/24.
 */
public class HotFragment extends BaseFragment implements Constant {

    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fast_scroller)
    FastScroller fastScroller;
    @BindView(R.id.empty_view)
    EmptyView emptyView;

    private List<AbstractFlexibleItem> items = new ArrayList<>();
    private HotWaresAdapter adatper;
    private int curPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private StatusEnum state = StatusEnum.ON_NORMAL;
    private ProgressItem progressItem;


    @Override
    public int getLayoutResID() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void createView() {
        super.createView();
        initView();
        refresh();
    }

    private void initView() {

        SinaRefreshView headerView = new SinaRefreshView(getActivity());
        headerView.setArrowResource(R.mipmap.refresh_head_arrow);
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setOnRefreshListener(refreshListenerAdapter);
        LoadingView loadingView = new LoadingView(getActivity());
        refreshLayout.setBottomView(loadingView);

        adatper = new HotWaresAdapter(items);
        progressItem = new ProgressItem(adatper, onClickListener);
        adatper.setEndlessScrollListener(endlessScrollListener, progressItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adatper);
        recyclerView.addItemDecoration(new SpaceItemDecoration(UiUtils.dp2px(DIMEN_NORMAL), SpaceItemDecoration.VERTICAL, true));

        adatper.setFastScroller(fastScroller, UiUtils.getColor(R.color.colorAccent));//Setup FastScroller after the Adapter has been added to the RecyclerView.
    }

    private void refresh() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.startRefresh();
            }
        });
    }

    private RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {

        @Override
        public void onRefresh(TwinklingRefreshLayout refreshLayout) {
            super.onRefresh(refreshLayout);
            refreshData();
        }

        @Override
        public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
            super.onLoadMore(refreshLayout);
            loadMoreData();
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_error:
                    progressItem.setStatus(StatusEnum.ON_LOAD_MORE);
                    loadMoreData();
                    break;
            }
        }
    };


    private int[] getProgressColors() {

        int[] colors = new int[SWIPE_REFRESH_LAYOUT_COLORS.length];

        for (int i = 0; i < SWIPE_REFRESH_LAYOUT_COLORS.length; i++) {
            colors[i] = UiUtils.getColor(SWIPE_REFRESH_LAYOUT_COLORS[i]);
        }

        return colors;
    }

    FlexibleAdapter.EndlessScrollListener endlessScrollListener = new FlexibleAdapter.EndlessScrollListener() {

        @Override
        public void noMoreLoad(int newItemsSize) {
        }

        @Override
        public void onLoadMore(int lastPosition, int currentPage) {
            progressItem.setStatus(StatusEnum.ON_LOAD_MORE);
            loadMoreData();
        }
    };


    private MaterialRefreshListener materialRefreshListener = new MaterialRefreshListener() {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            refreshData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            super.onRefreshLoadMore(materialRefreshLayout);
            if (curPage <= totalPage) {
                loadMoreData();
            } else {
                refreshLayout.finishLoadmore();
            }
        }
    };

    private void refreshData() {
        curPage = 1;
        state = StatusEnum.ON_NORMAL;
        requestData();
    }

    private void loadMoreData() {
        state = StatusEnum.ON_LOAD_MORE;
        requestData();
    }

    private void requestData() {

        String url = SERVER_URL + "wares/hot";

        OkGo
                .post(url)
                .params("curPage", curPage)
                .params("pageSize", pageSize)
                .cacheMode(com.lzy.okgo.cache.CacheMode.REQUEST_FAILED_READ_CACHE)
                .execute(new SimpleCallback() {

                    @Override
                    public void success(String s, Call call, okhttp3.Response response) {
                    }

                    @Override
                    public void after(String s, Exception e) {
                        finishRefresh();
                    }

                    @Override
                    public void afterSuccess(String s) {
                        super.afterSuccess(s);
                        curPage = ++curPage;
                        Type type = new TypeToken<Page<Wares>>() {
                        }.getType();
                        Page<Wares> page = new Gson().fromJson(s, type);
                        totalPage = page.getTotalPage();
                        showData(page);
                        emptyView.setEmptyView(items);
                    }

                    @Override
                    public void afterError(Exception e) {
                        super.afterError(e);
                        if (items.size() > 0) {
                            progressItem.setStatus(StatusEnum.ON_EMPTY);
                            adatper.onLoadMoreComplete(null, 2000);
                        } else {
                            emptyView.setEmptyView(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestData();
                                }
                            });
                        }
                    }
                });
    }

    private void finishRefresh() {
        switch (state) {
            case ON_NORMAL:
                refreshLayout.finishRefreshing();
                break;
            case ON_LOAD_MORE:
                refreshLayout.finishLoadmore();
                break;
        }
    }

    private void fail() {
        switch (state) {
            case ON_NORMAL:
                break;
            case ON_LOAD_MORE:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressItem.setStatus(StatusEnum.ON_ERROR);
                    }
                }, 500);
                break;
        }

    }

    private void showData(Page<Wares> page) {
        switch (state) {
            case ON_NORMAL: {
                items.clear();
                for (Wares wares : page.getList()) {
                    HotWaresItem item = new HotWaresItem(wares, recyclerView);
                    items.add(item);
                }
                adatper.notifyDataSetChanged();
            }
            break;
            case ON_LOAD_MORE: {
                final List<AbstractFlexibleItem> newItems = new ArrayList<>();
                for (Wares wares : page.getList()) {
                    HotWaresItem item = new HotWaresItem(wares, recyclerView);
                    newItems.add(item);
                }

                if (newItems.size() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressItem.setStatus(StatusEnum.ON_EMPTY);
                            adatper.onLoadMoreComplete(newItems, 2000);
                        }
                    }, 500);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adatper.onLoadMoreComplete(newItems);
                        }
                    }, 500);
                }

            }
            break;
        }

    }

}
