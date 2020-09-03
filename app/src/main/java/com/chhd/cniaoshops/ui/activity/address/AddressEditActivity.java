package com.chhd.cniaoshops.ui.activity.address;

import android.os.Bundle;
import android.view.MenuItem;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Address;
import com.chhd.cniaoshops.bean.User;
import com.chhd.cniaoshops.biz.UserLocalData;
import com.chhd.cniaoshops.global.App;
import com.chhd.cniaoshops.http.bmob.SimpleUpdateListener;
import com.chhd.per_library.util.SoftKeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class AddressEditActivity extends BaseAddressActivity {

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pos = getIntent().getIntExtra("pos", 0);
        Address address = App.user.getAddresses().get(pos);

        getSupportActionBar().setTitle(R.string.modify_address);

        etConsignee.setText(address.getConsignee());
        etNumber.setText(address.getNumber());
        tvArea.setText(address.getArea());
        etAddress.setText(address.getDetailAddress());

        SoftKeyboardUtils.showSoftInput(etConsignee);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DEFAULT_ID:
                requestEditAddress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestEditAddress() {
        pos = getIntent().getIntExtra("pos", 0);
        final Address address = App.user.getAddresses().get(pos);
        address.setConsignee(etConsignee.getText().toString());
        address.setNumber(etNumber.getText().toString());
        address.setArea(tvArea.getText().toString());
        address.setDetailAddress(etAddress.getText().toString());

        List<Address> newList = new ArrayList<>();
        newList.addAll(App.user.getAddresses());

        User newUser = new User();
        newUser.setAddresses(newList);
        newUser.update(BmobUser.getCurrentUser().getObjectId(), new SimpleUpdateListener(this) {
            @Override
            public void success() {
                UserLocalData.putUser(App.user);
                finish();
            }
        });
    }
}
