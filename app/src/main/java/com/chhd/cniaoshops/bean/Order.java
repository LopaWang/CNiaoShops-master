package com.chhd.cniaoshops.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by CWQ on 2017/4/29.
 */

public class Order extends BmobObject {

    public static final int STATUS_PAY_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -1; //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0; //：待支付的订单

    private long orderNum;

    private int status;

    private long creatTime;

    private List<ShoppingCart> carts;

    private float amount;

    private Address address;

    private User user;

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public List<ShoppingCart> getCarts() {
        return carts;
    }

    public void setCarts(List<ShoppingCart> carts) {
        this.carts = carts;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
