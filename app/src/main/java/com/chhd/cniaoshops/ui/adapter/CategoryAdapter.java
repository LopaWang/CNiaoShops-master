package com.chhd.cniaoshops.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Category;
import com.chhd.per_library.ui.base.SimpleAdapter;
import com.chhd.per_library.ui.base.SimpleHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CWQ on 2017/3/26.
 */

public class CategoryAdapter extends SimpleAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.list_item_category, null);
        }
        Category category = getItem(position);
        Holder holder = Holder.getHodler(convertView);
        holder.tvCategory.setText(category.getName());

        return convertView;
    }

    static class Holder extends SimpleHolder {

        @BindView(R.id.tv_category)
        TextView tvCategory;

        public Holder(View convertView) {
            super(convertView);
        }

        public static Holder getHodler(View converview) {
            Holder holder = (Holder) converview.getTag();
            if (holder == null) {
                holder = new Holder(converview);
                converview.setTag(holder);
            }
            return holder;
        }
    }
}
