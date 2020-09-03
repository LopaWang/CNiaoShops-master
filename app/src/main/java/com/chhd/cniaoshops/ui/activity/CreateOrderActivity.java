package com.chhd.cniaoshops.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Address;
import com.chhd.cniaoshops.bean.Order;
import com.chhd.cniaoshops.bean.ShoppingCart;
import com.chhd.cniaoshops.biz.CartProvider;
import com.chhd.cniaoshops.global.App;
import com.chhd.cniaoshops.http.SimpleCallback;
import com.chhd.cniaoshops.http.bmob.SimpleSaveListener;
import com.chhd.cniaoshops.http.bmob.SimpleUpdateListener;
import com.chhd.cniaoshops.ui.activity.address.AddressListActivity;
import com.chhd.cniaoshops.ui.adapter.WaresOrderAdapter;
import com.chhd.cniaoshops.ui.base.activity.BaseActivity;
import com.chhd.cniaoshops.ui.decoration.DividerItemDecoration;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.lzy.okgo.OkGo;
import com.pingplusplus.android.Pingpp;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class CreateOrderActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_wares)
    RecyclerView rvWares;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.rg_pay_mode)
    RadioGroup rgPayMode;
    @BindView(R.id.tv_name_and_num)
    TextView tvNameAndNum;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    private ArrayList<ShoppingCart> carts;
    private Address address;
    private String orderId;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        initView();
    }

    private void initView() {

        if (App.user.getAddresses().isEmpty()) {
            tvNameAndNum.setVisibility(View.GONE);
            tvAddress.setVisibility(View.GONE);
            tvAddress.setText(R.string.please_fill_in_the_receiving_address);
        } else {
            tvNameAndNum.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.VISIBLE);

            for (Address address : App.user.getAddresses()) {
                if (address.getDefault()) {
                    this.address = address;
                    tvNameAndNum.setText(String.format("%1$s (%2$s)", address.getConsignee(), address.getNumber()));
                    tvAddress.setText(address.getArea() + address.getDetailAddress());
                    break;
                }
            }
        }

        carts = (ArrayList<ShoppingCart>) getIntent().getSerializableExtra("carts");
        WaresOrderAdapter adapter = new WaresOrderAdapter(carts);
        rvWares.setAdapter(adapter);
        rvWares.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvWares.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));

        showTotalPrice(carts);
    }

    private void showTotalPrice(List<ShoppingCart> carts) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        float total = getTotalPrice(carts);
        tvTotalPrice.setText(String.format(getString(R.string.total), numberFormat.format(total)));
    }

    private float getTotalPrice(List<ShoppingCart> carts) {
        float sum = 0;
        for (ShoppingCart cart : carts) {
            if (cart.isChecked())
                sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.create_order);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_create_order;
    }

    @OnClick({R.id.btn_pay, R.id.address})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                if (address == null) {
                    ToastyUtils.warning(R.string.please_fill_in_the_receiving_address);
                    return;
                }
                requestCreateOrder();
                break;
            case R.id.address:
                Intent intent = new Intent(this, AddressListActivity.class);
                startActivityForResult(intent, REQUEST_ADDRESS_LIST_ACTIVITY);
                break;
        }
    }

    private void requestCreateOrder() {
        order = new Order();
        order.setStatus(Order.STATUS_PAY_WAIT);
        order.setOrderNum(System.currentTimeMillis());
        order.setCreatTime(System.currentTimeMillis());
        order.setCarts(carts);
        order.setAmount(getTotalPrice(carts));
        order.setAddress(address);
        order.setUser(App.user);
        order.save(new SimpleSaveListener<String>(this) {

            @Override
            public void success(String objectId) {
                for (ShoppingCart cart : carts) {
                    new CartProvider().delete(cart);
                }
                CreateOrderActivity.this.orderId = objectId;
                requestPay();
            }
        });
    }

    private void requestPay() {
        RadioButton rb = ButterKnife.findById(this, rgPayMode.getCheckedRadioButtonId());
        OkGo
                .post("http://218.244.151.190/demo/charge")
                .params("channel", (String) rb.getTag())
                .params("amount", "1")
                .execute(new SimpleCallback(this) {
                    @Override
                    public void success(String s, Call call, Response response) {
                        Pingpp.createPayment(CreateOrderActivity.this, s);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            String result = data.getExtras().getString("pay_result");
       /* 处理返回值
        * "success" - 支付成功
        * "fail"    - 支付失败
        * "cancel"  - 取消支付
        * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
        * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
        */
            String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
            String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

            int status;
            LoggerUtils.i("result: " + result);
            switch (result) {
                case "success":
                    status = Order.STATUS_PAY_SUCCESS;
                    break;
                case "fail":
                    ToastyUtils.error(errorMsg);
                    status = Order.STATUS_PAY_FAIL;
                    break;
                default:
                    ToastyUtils.warning(errorMsg);
                    status = Order.STATUS_PAY_WAIT;
                    break;
            }
            order.setValue("status", status);
            order.update(new SimpleUpdateListener() {

                @Override
                public void success() {
                }
            });
            finish();

        }
        switch (requestCode) {
            case REQUEST_ADDRESS_LIST_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    address = (Address) data.getSerializableExtra("address");
                    tvNameAndNum.setText(String.format("%1$s (%2$s)", address.getConsignee(), address.getNumber()));
                    tvAddress.setText(address.getArea() + address.getDetailAddress());
                }
                break;
        }
    }
}
