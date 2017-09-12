package com.example.nazb.styleomega;

/**
 * Created by nazB on 8/27/2017.
 */

public class Address {
    private int address_id;
    private String house_no;
    private String main_road;
    private String sub_road;
    private String landmark;
    private String city;
    private String contact_no;

    public Address(int address_id, String house_no, String main_road, String sub_road, String landmark, String city, String contact_no) {
        this.address_id = address_id;
        this.house_no = house_no;
        this.main_road = main_road;
        this.sub_road = sub_road;
        this.landmark = landmark;
        this.city = city;
        this.contact_no = contact_no;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getMain_road() {
        return main_road;
    }

    public void setMain_road(String main_road) {
        this.main_road = main_road;
    }

    public String getSub_road() {
        return sub_road;
    }

    public void setSub_road(String sub_road) {
        this.sub_road = sub_road;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }
}
