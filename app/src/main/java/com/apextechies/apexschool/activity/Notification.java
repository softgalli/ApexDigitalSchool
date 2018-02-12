package com.apextechies.apexschool.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.apextechies.apexschool.R;
import com.apextechies.apexschool.adapter.NotificationAdapter;
import com.apextechies.apexschool.calender.PreferenceName;
import com.apextechies.apexschool.common.BaseActivity;
import com.apextechies.apexschool.intrface.ClickListener;
import com.apextechies.apexschool.model.NotificationDateList;
import com.apextechies.apexschool.model.NotificationModel;
import com.apextechies.apexschool.retrofit.DownlodableCallback;
import com.apextechies.apexschool.retrofit.RetrofitDataProvider;
import com.apextechies.apexschool.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 1/27/2018.
 */

public class Notification extends BaseActivity {
    private static final String TAG = "";
    @BindView(R.id.rv_common)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<NotificationDateList> notification_list;
    private RetrofitDataProvider retrofitDataProvider;
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        mActivity = this;
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);
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
        //Use Here your common internet checker method
        if (ViewUtils.isOnline(mActivity)) {
            retrofitDataProvider.notification("apexschool_1001", new DownlodableCallback<NotificationModel>() {
                @Override
                public void onSuccess(final NotificationModel result) {
                    //  closeDialog();
                    if (result.getStatus().contains(PreferenceName.TRUE)) {
                        notification_list = result.getData();
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
        } else
            ViewUtils.showNoInternetConnectionDialog(mActivity);
    }

    private void setAdapter() {
        recyclerView.setAdapter(new NotificationAdapter(this, notification_list, R.layout.notification_item, new ClickListener() {
            @Override
            public void Onclick(int pos) {

            }
        }));
    }

    private void initWidgit() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notification_list = new ArrayList<>();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
