package com.example.nazb.styleomega;

import android.widget.ArrayAdapter;

/**
 * Created by nazB on 8/28/2017.
 */

public class CartItems {
    int cart_id;
    String product_name;
    int qty;
    String product_image;
    int product_price;
    int item_id;

    public CartItems(int cart_id, String product_name, int qty, String product_image, int product_price, int item_id) {
        this.cart_id = cart_id;
        this.product_name = product_name;
        this.qty = qty;
        this.product_image = product_image;
        this.product_price = product_price;
        this.item_id = item_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }
}
