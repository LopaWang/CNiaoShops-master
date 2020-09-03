package com.chhd.cniaoshops.ui.activity.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Address;
import com.chhd.cniaoshops.bean.User;
import com.chhd.cniaoshops.biz.UserLocalData;
import com.chhd.cniaoshops.global.App;
import com.chhd.cniaoshops.http.bmob.SimpleUpdateListener;
import com.chhd.cniaoshops.ui.activity.address.AddressAddActivity;
import com.chhd.cniaoshops.ui.activity.address.AddressEditActivity;
import com.chhd.cniaoshops.ui.adapter.AddressAdapter;
import com.chhd.cniaoshops.ui.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;

public class AddressListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_address)
    RecyclerView rvAddress;

    private AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        adapter = new AddressAdapter(rvAddress, App.user.getAddresses());
        adapter.setCustomEmptyView();
        adapter.setOnItemChildClickListener(onItemChildClickListener);
        adapter.setOnItemClickListener(onItemClickListener);
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.setAdapter(adapter);
    }

    private BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Intent data = new Intent();
            data.putExtra("address", App.user.getAddresses().get(position));
            setResult(RESULT_OK, data);
            finish();

        }
    };

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.delivery_address);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_address_list;
    }


    private BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {

        @Override
        public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            switch (view.getId()) {
                case R.id.check_box:
                    setDefaultAddress(position);
                    break;
                case R.id.tv_edit:
                    Intent intent = new Intent(context, AddressEditActivity.class);
                    intent.putExtra("pos", position);
                    startActivity(intent);
                    break;
                case R.id.tv_delete:
                    requestDeleteAddress(position);
                    break;
            }
            return false;
        }
    };

    private void setDefaultAddress(int pos) {
        for (Address address : App.user.getAddresses()) {
            if (address == App.user.getAddresses().get(pos)) {
                address.setDefault(true);
            } else {
                address.setDefault(false);
            }
        }
        adapter.notifyDataSetChanged();
        requestSetDefaultAddress();
    }

    private void requestSetDefaultAddress() {
        List<Address> newList = new ArrayList<>();
        newList.addAll(adapter.getData());
        User newUser = new User();
        newUser.setAddresses(newList);
        newUser.update(BmobUser.getCurrentUser().getObjectId(), new SimpleUpdateListener(this) {

            @Override
            public void success() {
                UserLocalData.putUser(App.user);
            }
        });
    }

    private void requestDeleteAddress(final int pos) {
        List<Address> newList = new ArrayList<>();
        newList.addAll(adapter.getData());
        newList.remove(pos);
        User newUser = new User();
        newUser.setAddresses(newList);
        newUser.update(BmobUser.getCurrentUser().getObjectId(), new SimpleUpdateListener(this) {

            @Override
            public void success() {
                adapter.remove(pos);
                UserLocalData.putUser(App.user);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, MENU_DEFAULT_ID, 0, R.string.add);
        item.setIcon(R.drawable.ic_add_white_24dp);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DEFAULT_ID:
                Intent intent = new Intent(this, AddressAddActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
