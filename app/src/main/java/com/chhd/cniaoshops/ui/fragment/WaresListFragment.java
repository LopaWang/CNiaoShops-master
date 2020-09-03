package com.chhd.cniaoshops.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Page;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.http.SimpleStringCallback;
import com.chhd.cniaoshops.ui.StatusEnum;
import com.chhd.cniaoshops.ui.adapter.WaresAdapter;
import com.chhd.cniaoshops.ui.base.fragment.LazyFragment;
import com.chhd.cniaoshops.ui.decoration.SpaceItemDecoration;
import com.chhd.cniaoshops.ui.decoration.StaggeredSpaceItemDecoration;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.JsonUtils;
import com.chhd.per_library.util.UiUtils;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by CWQ on 2017/4/8.
 */

public class WaresListFragment extends LazyFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.swipe_to_load_layout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_target)
    RecyclerView rvWares;

    private final int LIST_GRID = 30;
    private final int LIST_VER = 31;
    private int listMode = LIST_GRID;

    private int curPage = 1;
    private int pageSize = 10;
    private long campaignId = 0;
    private int orderBy = 0;
    private StatusEnum state;

    private List<Wares> items = new ArrayList<>();
    private WaresAdapter adapter;

    public static WaresListFragment newInstance(String title, long campaignId, int orderBy) {
        WaresListFragment fragment = new WaresListFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putLong("campaignId", campaignId);
        args.putInt("orderBy", orderBy);
        fragment.setArguments(args);
        return fragment;
    }

    public WaresListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            campaignId = getArguments().getLong("campaignId");
            orderBy = getArguments().getInt("orderBy");
        }
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_wares_list;
    }

    @Override
    protected void lazyLoad() {
        if (listMode == LIST_VER) {
            showVerList();
        } else {
            showGridList();
        }

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(false);

        refresh();
    }

    private void refreshData() {
        curPage = 1;
        state = StatusEnum.ON_NORMAL;
        requestData();

    }

    private void loadMoreData() {
        state = StatusEnum.ON_LOAD_MORE;
        requestData();
    }

    private void refresh() {
        refreshData();
    }

    private void requestData() {
        String url = SERVER_URL + "wares/campaign/list";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("campaignId", "" + campaignId)
                .addParams("orderBy", "" + orderBy)
                .addParams("curPage", "" + curPage)
                .addParams("pageSize", "" + pageSize)
                .build()
                .execute(new SimpleStringCallback() {

                    @Override
                    public void before(Request request, int id) {
                        super.before(request, id);
                        adapter.setLoadingView();
                    }

                    @Override
                    public void success(String response, int id) {
                        try {
                            curPage = ++curPage;
                            Page<Wares> page = JsonUtils.fromJson(response, new TypeToken<Page<Wares>>() {
                            }.getType());
                            showWaresData(page);
                            if (items.size() > 0) {
                                swipeToLoadLayout.setLoadMoreEnabled(true);
                            }
                        } catch (Exception e) {
                            LoggerUtils.e(e);
                        }
                    }

                    @Override
                    public void error(Call call, Exception e, int id) {
                        super.error(call, e, id);
                        adapter.setCustomEmptyView(networkErrorClickListener);
                    }

                    @Override
                    public void after(int id) {
                        super.after(id);
                        if (swipeToLoadLayout.isRefreshing()) {
                            swipeToLoadLayout.setRefreshing(false);
                        } else if (swipeToLoadLayout.isLoadingMore()) {
                            swipeToLoadLayout.setLoadingMore(false);
                        }
                    }
                });
    }

    private void showWaresData(Page<Wares> page) {
        switch (state) {
            case ON_NORMAL:
                adapter.refreshData(page.getList());
                adapter.setCustomEmptyView();
                break;
            case ON_LOAD_MORE:
                adapter.addAll(page.getList());
                break;
        }
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                    swipeToLoadLayout.setLoadingMore(true);
                }
            }
        }
    };

    private RecyclerView.ItemDecoration verItemDec = new SpaceItemDecoration(UiUtils.dp2px(DIMEN_NORMAL), SpaceItemDecoration.VERTICAL, true);
    private RecyclerView.ItemDecoration gridItemDec = new StaggeredSpaceItemDecoration(UiUtils.dp2px(DIMEN_NORMAL / 2));

    public void setGridList() {
        listMode = LIST_GRID;
        showGridList();
    }

    private void showGridList() {
        adapter = new WaresAdapter(rvWares, R.layout.staggered_item_wares, items);
        adapter.setCustomEmptyView();
        rvWares.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvWares.setAdapter(adapter);
        rvWares.removeItemDecoration(verItemDec);
        rvWares.removeItemDecoration(gridItemDec);
        rvWares.addItemDecoration(gridItemDec);
        rvWares.setPadding(UiUtils.dp2px(DIMEN_NORMAL / 2), UiUtils.dp2px(DIMEN_NORMAL / 2), UiUtils.dp2px(DIMEN_NORMAL / 2), UiUtils.dp2px(DIMEN_NORMAL / 2));
        rvWares.removeOnScrollListener(onScrollListener);
        rvWares.addOnScrollListener(onScrollListener);
    }

    private void showVerList() {
        adapter = new WaresAdapter(rvWares, R.layout.list_item_wares, items);
        adapter.setCustomEmptyView();
        rvWares.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvWares.setAdapter(adapter);
        rvWares.removeItemDecoration(verItemDec);
        rvWares.removeItemDecoration(gridItemDec);
        rvWares.addItemDecoration(verItemDec);
        rvWares.setPadding(0, 0, 0, 0);
        rvWares.removeOnScrollListener(onScrollListener);
        rvWares.addOnScrollListener(onScrollListener);
    }

    public void setVerList() {
        listMode = LIST_VER;
        showVerList();
    }

    private View.OnClickListener networkErrorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            refresh();
        }
    };

    @Override
    public void onLoadMore() {
        loadMoreData();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }
}
