package com.chhd.cniaoshops.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Order;
import com.chhd.cniaoshops.ui.adapter.OrderAdapter;
import com.chhd.cniaoshops.ui.base.fragment.BaseFragment;
import com.chhd.cniaoshops.ui.decoration.SpaceItemDecoration;
import com.chhd.per_library.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderFragment extends BaseFragment {

    @BindView(R.id.rv_order)
    RecyclerView rvOrder;

    private List<Order> orders = new ArrayList<>();

    public OrderFragment() {
    }

    public static OrderFragment newInstance(String title, List<Order> list) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("orders", (ArrayList) list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initData();
        }
    }

    private void initData() {
        String title = getArguments().getString("title");
        List<Order> list = (List<Order>) getArguments().getSerializable("orders");
        if (title.equals(UiUtils.getString(R.string.whole))) {
            orders.addAll(list);
        }
        if (title.equals(UiUtils.getString(R.string.unpaid))) {
            for (Order order : list) {
                if (order.getStatus() == Order.STATUS_PAY_WAIT) {
                    orders.add(order);
                }
            }
        }
        if (title.equals(UiUtils.getString(R.string.pay_success))) {
            for (Order order : list) {
                if (order.getStatus() == Order.STATUS_PAY_SUCCESS) {
                    orders.add(order);
                }
            }
        }
        if (title.equals(UiUtils.getString(R.string.pay_fail))) {
            for (Order order : list) {
                if (order.getStatus() == Order.STATUS_PAY_FAIL) {
                    orders.add(order);
                }
            }
        }
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_order;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OrderAdapter adapter = new OrderAdapter(rvOrder, orders);
        adapter.setCustomEmptyView();
        rvOrder.setAdapter(adapter);
        rvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrder.addItemDecoration(new SpaceItemDecoration(UiUtils.dp2px(5), SpaceItemDecoration.VERTICAL, 0, UiUtils.dp2px(5), 0, 0));
    }
}
