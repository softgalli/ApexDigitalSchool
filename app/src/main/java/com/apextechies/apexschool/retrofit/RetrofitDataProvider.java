package com.apextechies.apexschool.retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.apextechies.apexschool.common.BaseActivity;
import com.apextechies.apexschool.model.NotificationModel;
import com.apextechies.apexschool.calender.WebServices;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shankar on 1/27/2018.
 */

public class RetrofitDataProvider extends BaseActivity implements ServiceMethods {
    private Context context;

    public RetrofitDataProvider(Context context) {
        this.context = context;
    }

    private ApiRetrofitService createRetrofitService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiRetrofitService.class);
    }

    @Override
    public void notification(String school_id, final DownlodableCallback<NotificationModel> callback) {
        createRetrofitService().otpLogin(school_id).enqueue(
                new Callback<NotificationModel>() {
                    @Override
                    public void onResponse(@NonNull Call<NotificationModel> call, @NonNull final Response<NotificationModel> response) {
                        if (response.isSuccessful()) {

                            NotificationModel mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);

                        } else

                        {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                                /*checkStatusCode(context, response.code(), response.message(), new OnCheckStatusCode() {
                                    @Override
                                    public void statuscode(int code) {
                                        callback.onFailure("error");
                                    }
                                });*/
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NotificationModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());

                    }
                }
        );
    }
}