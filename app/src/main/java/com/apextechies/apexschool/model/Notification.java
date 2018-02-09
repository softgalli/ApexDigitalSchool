package com.apextechies.apexschool.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shankar on 1/29/2018.
 */

public class Notification {

    @SerializedName("notification_id")
    String notification_id;
    @SerializedName("school_id")
    String school_id;
    @SerializedName("created_by")
    String created_by;
    @SerializedName("notification_title")
    String notification_title;
    @SerializedName("notification_description")
    String notification_description;
    @SerializedName("created_date")
    String created_date;
    @SerializedName("created_time")
    String created_time;
    @SerializedName("notification_image")
    ArrayList<NotificationImage> notification_image;

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getNotification_description() {
        return notification_description;
    }

    public void setNotification_description(String notification_description) {
        this.notification_description = notification_description;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public ArrayList<NotificationImage> getNotification_image() {
        return notification_image;
    }

    public void setNotification_image(ArrayList<NotificationImage> notification_image) {
        this.notification_image = notification_image;
    }
}
