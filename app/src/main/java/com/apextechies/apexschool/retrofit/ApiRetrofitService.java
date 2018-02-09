package com.apextechies.apexschool.retrofit;

import com.apextechies.apexschool.model.NotificationModel;
import com.apextechies.apexschool.calender.WebServices;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Shankar on 1/27/2018.
 */

public interface ApiRetrofitService {
    @POST(WebServices.NOTIFICATION)
    @FormUrlEncoded
    Call<NotificationModel> otpLogin(@Field("school_id") String school_id);
}
