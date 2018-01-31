package com.apextechies.apexschool.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shankar on 1/27/2018.
 */

public class NotificationDateList {


    @SerializedName("notification_date")
    String notification_date;

    public String getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(String notification_date) {
        this.notification_date = notification_date;
    }

    public ArrayList<Notification> getNotification() {
        return notification;
    }

    public void setNotification(ArrayList<Notification> notification) {
        this.notification = notification;
    }

    @SerializedName("notification")
    ArrayList<Notification> notification;



}
