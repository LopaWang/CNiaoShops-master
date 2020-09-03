package com.chhd.cniaoshops.ui.activity.address;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Province;
import com.chhd.cniaoshops.ui.activity.PlaceChooseActivity;
import com.chhd.cniaoshops.ui.base.activity.HideSoftInputActivity;
import com.chhd.cniaoshops.util.JsonUtils;
import com.chhd.per_library.util.UiUtils;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BaseAddressActivity extends HideSoftInputActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_consignee)
    EditText etConsignee;
    @BindView(R.id.et_number)
    EditText etNumber;

    private final int RQUEST_FORM_PLACE_CHOOSE_ACTIVITY = 100;

    private OptionsPickerView pickerView;
    private List<Province> provinces;
    private List<List<String>> cities;
    private List<List<List<String>>> districts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        initData();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_base_address;
    }

    protected void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        provinces = JsonUtils.fromJson(getJson(this, "province.json"), new TypeToken<List<Province>>() {
        }.getType());
        cities = new ArrayList<>();
        districts = new ArrayList<>();

        for (int i = 0; i < provinces.size(); i++) {//遍历省份
            List<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            List<List<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < provinces.get(i).getCity().size(); c++) {//遍历该省份的所有城市
                String CityName = provinces.get(i).getCity().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (provinces.get(i).getCity().get(c).getArea() == null
                        || provinces.get(i).getCity().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < provinces.get(i).getCity().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = provinces.get(i).getCity().get(c).getArea().get(d);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            cities.add(CityList);
            /**
             * 添加地区数据
             */
            districts.add(Province_AreaList);
        }


        //设置选中项文字颜色
        // default is true
        pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String province = provinces.get(options1).getPickerViewText();
                String city = cities.get(options1).get(options2);
                String district = districts.get(options1).get(options2).get(options3);
                String tx;
                if (!province.equals(city)) {
                    tx = province + city + district;
                } else {
                    tx = city + district;
                }
                tvArea.setText(tx);
            }
        })
                .setTitleText(getString(R.string.select_area))
                .setSubmitText(getString(R.string.confirm))
                .setCancelText(getString(R.string.cancel))
                .setDividerColor(Color.parseColor("#CCCCCC"))
                .setTextColorCenter(UiUtils.getColor(R.color.def_text_black)) //设置选中项文字颜色
                .setSubmitColor(UiUtils.getColor(R.color.def_text_mid))
                .setCancelColor(UiUtils.getColor(R.color.def_text_mid))
                .setContentTextSize(16)
                .setTitleSize(14)
                .setSubCalSize(14)
                .setLineSpacingMultiplier(2.0f)
                .setOutSideCancelable(true)// default is true
                .build();
        pickerView.setPicker(provinces, cities, districts);
    }

    @OnClick({R.id.tv_area, R.id.iv_loc})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_area:
                pickerView.show();
                break;
            case R.id.iv_loc:
                Intent intent = new Intent(this, PlaceChooseActivity.class);
                startActivityForResult(intent, RQUEST_FORM_PLACE_CHOOSE_ACTIVITY);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, MENU_DEFAULT_ID, 0, R.string.add);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_done_white_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RQUEST_FORM_PLACE_CHOOSE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String area = data.getStringExtra("area");
                    String address = data.getStringExtra("address");
                    tvArea.setText(area);
                    etAddress.setText(address);
                }
                break;
        }
    }

    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
