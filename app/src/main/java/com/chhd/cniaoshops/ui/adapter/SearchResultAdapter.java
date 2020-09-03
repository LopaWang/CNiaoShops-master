package com.chhd.cniaoshops.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.chhd.cniaoshops.R;
import com.chhd.per_library.ui.base.SimpleAdapter;
import com.chhd.per_library.ui.base.SimpleHolder;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by CWQ on 2017/4/27.
 */

public class SearchResultAdapter extends SimpleAdapter<PoiItem> {

    private int selectedPosition = 0;

    public SearchResultAdapter(Context context, List<PoiItem> data) {
        super(context, data);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_holder_result, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.bindView(position);
        return convertView;
    }

    class Holder extends SimpleHolder {

        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.text_title_sub)
        TextView textSubTitle;
        @BindView(R.id.image_check)
        ImageView imageCheck;

        public Holder(View itemView) {
            super(itemView);
        }

        public void bindView(int position) {
            if (position >= data.size())
                return;

            PoiItem poiItem = data.get(position);

            textTitle.setText(poiItem.getTitle());
            textSubTitle.setText(poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());

            imageCheck.setVisibility(position == selectedPosition ? View.VISIBLE : View.INVISIBLE);
            textSubTitle.setVisibility((position == 0 && poiItem.getPoiId().equals("regeo")) ? View.GONE : View.VISIBLE);
        }
    }
}
