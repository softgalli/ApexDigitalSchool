package com.apextechies.apexschool.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.apextechies.apexschool.R;
import com.apextechies.apexschool.adapter.NotificationAdapter;
import com.apextechies.apexschool.common.BaseActivity;
import com.apextechies.apexschool.intrface.ClickListener;
import com.apextechies.apexschool.model.NotificationList;
import com.apextechies.apexschool.model.NotificationModel;
import com.apextechies.apexschool.retrofit.DownlodableCallback;
import com.apextechies.apexschool.retrofit.RetrofitDataProvider;
import com.apextechies.apexschool.utilz.PreferenceName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 1/27/2018.
 */

public class Notification extends BaseActivity {
    @BindView(R.id.rv_common)  RecyclerView recyclerView;

    private static final String TAG = "";
    private ArrayList<NotificationList> notification_list;
    private RetrofitDataProvider retrofitDataProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
        gwtCurrentDate();
        initWidgit();
        getNotificationList();
    }

    private void gwtCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        Log.e("Current date => ", formattedDate);
    }

    private void getNotificationList() {
        retrofitDataProvider.notification("apexschool_1001", new DownlodableCallback<NotificationModel>() {
            @Override
            public void onSuccess(final NotificationModel result) {
              //  closeDialog();


                if (result.getStatus().contains(PreferenceName.TRUE)) {

                    notification_list=result.getData();
                    setAdapter();

                }

            }

            @Override
            public void onFailure(String error) {
               // closeDialog();
            }

            @Override
            public void onUnauthorized(int errorNumber) {

            }
        });
    }

    private void setAdapter() {
        recyclerView.setAdapter(new NotificationAdapter(this,notification_list, R.layout.notification_item, new ClickListener() {
            @Override
            public void Onclick(int pos) {

            }
        }));
    }

    private void initWidgit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notification_list = new ArrayList<>();
    }
}
