package com.apextechies.apexschool.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shankar on 1/27/2018.
 */

public class NotificationModel {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<NotificationList> getData() {
        return data;
    }

    public void setData(ArrayList<NotificationList> data) {
        this.data = data;
    }

    @SerializedName("status")
    String status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    ArrayList<NotificationList> data;
}