package com.apextechies.apexschool.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shankar on 1/27/2018.
 */

public class NotificationModel {
    @SerializedName("status")
    String status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    ArrayList<NotificationDateList> data;

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

    public ArrayList<NotificationDateList> getData() {
        return data;
    }

    public void setData(ArrayList<NotificationDateList> data) {
        this.data = data;
    }
}
