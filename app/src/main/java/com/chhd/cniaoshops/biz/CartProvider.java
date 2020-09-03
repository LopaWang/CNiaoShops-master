package com.chhd.cniaoshops.biz;

import android.text.TextUtils;
import android.util.SparseArray;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.ShoppingCart;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.chhd.cniaoshops.util.JsonUtils;
import com.chhd.per_library.util.SpUtils;
import com.chhd.per_library.util.UiUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2017/4/3.
 */

public class CartProvider {

    private final String KEY_SHOPPING_CARTS = "shopping_carts";

    private SparseArray<ShoppingCart> array = new SparseArray<>();

    public CartProvider() {
        listToArray();
    }

    private void listToArray() {
        List<ShoppingCart> items = getDataFromLocal();
        for (ShoppingCart cart : items) {
            array.put(cart.getId().intValue(), cart);
        }
    }

    public void put(ShoppingCart cart) {
        ShoppingCart item = array.get(cart.getId().intValue());
        if (item != null) {
            item.setCount(item.getCount() + 1);
        } else {
            item = cart;
            item.setCount(1);
        }
        array.put(item.getId().intValue(), item);
        commit();
        ToastyUtils.success(String.format("%1$s：%2$s", UiUtils.getString(R.string.add_shopping_cart_success), cart.getName()));
    }

    public void put(Wares wares) {
        put(convertData(wares));
    }

    private ShoppingCart convertData(Wares wares) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(wares.getId());
        cart.setDescription(wares.getDescription());
        cart.setImgUrl(wares.getImgUrl());
        cart.setName(wares.getName());
        cart.setPrice(wares.getPrice());
        return cart;
    }

    /**
     * listToJson
     */
    private void commit() {
        List<ShoppingCart> items = sparseToList();
        SpUtils.putString(KEY_SHOPPING_CARTS, JsonUtils.toJSON(items));
    }

    /**
     * SparseToList
     *
     * @return
     */
    private List<ShoppingCart> sparseToList() {
        List<ShoppingCart> items = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            items.add(array.valueAt(i));
        }
        return items;
    }

    public void update(ShoppingCart cart) {
        array.put(cart.getId().intValue(), cart);
        commit();
    }

    public void delete(ShoppingCart cart) {
        array.delete(cart.getId().intValue());
        commit();
    }

    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }

    public List<ShoppingCart> getDataFromLocal() {
        String json = SpUtils.getString(KEY_SHOPPING_CARTS, "");
        List<ShoppingCart> items = new ArrayList<>();
        if (!TextUtils.isEmpty(json)) {
            items = JsonUtils.fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return items;
    }
}
