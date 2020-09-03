package com.chhd.cniaoshops.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Address;
import com.chhd.cniaoshops.ui.base.adapter.SimpleAdapter;
import com.chhd.per_library.ui.base.SimpleHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by CWQ on 2017/4/28.
 */

public class AddressAdapter extends SimpleAdapter<Address, BaseViewHolder> {

    public AddressAdapter(RecyclerView recyclerView, List<Address> data) {
        super(recyclerView, R.layout.list_item_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Address item) {
        Holder holder = new Holder(helper.itemView);
        holder.checkBox.setChecked(item.getDefault());
        holder.tvConsignee.setText(item.getConsignee());
        holder.tvNumber.setText(String.format("(%s)", item.getNumber()));
        holder.tvAddress.setText(item.getArea() + item.getDetailAddress());
        holder.checkBox.setEnabled(!item.getDefault());
        helper.addOnClickListener(R.id.check_box);
        helper.addOnClickListener(R.id.tv_edit);
        helper.addOnClickListener(R.id.tv_delete);
    }

    class Holder extends SimpleHolder {

        @BindView(R.id.check_box)
        CheckBox checkBox;
        @BindView(R.id.tv_consignee)
        TextView tvConsignee;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_address)
        TextView tvAddress;

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
