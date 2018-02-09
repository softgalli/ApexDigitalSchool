package com.apextechies.apexschool.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shankar on 1/27/2018.
 */

public class NotificationImage {
    @SerializedName("notification_image_id")
    String notification_image_id;
    @SerializedName("notification_image")
    String notification_image;

    public String getNotification_image_id() {
        return notification_image_id;
    }

    public void setNotification_image_id(String notification_image_id) {
        this.notification_image_id = notification_image_id;
    }

    public String getNotification_image() {
        return notification_image;
    }

    public void setNotification_image(String notification_image) {
        this.notification_image = notification_image;
    }
}
