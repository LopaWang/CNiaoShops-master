package com.chhd.cniaoshops.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by CWQ on 2017/4/24.
 */

public class Province implements IPickerViewData {

    private String name;

    private List<City> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
