package com.example.nazb.styleomega;

/**
 * Created by nazB on 8/30/2017.
 */

public class Inquiry {

    int inquiry_id;
    String item;
    String user_name;
    String description;
    String date;
    String reply_description;
    String reply_date;

    public Inquiry(int inquiry_id, String user_name, String item, String description, String date, String reply_description, String reply_date) {
        this.inquiry_id = inquiry_id;
        this.user_name = user_name;
        this.item = item;
        this.description = description;
        this.date = date;
        this.reply_description = reply_description;
        this.reply_date = reply_date;
    }

    public int getInquiry_id() {
        return inquiry_id;
    }

    public void setInquiry_id(int inquiry_id) {
        this.inquiry_id = inquiry_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReply_description() {
        return reply_description;
    }

    public void setReply_description(String reply_description) {
        this.reply_description = reply_description;
    }

    public String getReply_date() {
        return reply_date;
    }

    public void setReply_date(String reply_date) {
        this.reply_date = reply_date;
    }
}
