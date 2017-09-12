package com.example.nazb.styleomega;

/**
 * Created by nazB on 8/21/2017.
 */

public class Products {
    private String prod_image;
    private String product_name;
    private String product_desc;
    private String product_price;

    public Products(String prod_image, String product_name, String product_desc, String product_price) {
        this.prod_image = prod_image;
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.product_price = product_price;
    }

    public String getProd_image() {
        return prod_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProd_image(int prod_id) {
        this.prod_image = prod_image;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
