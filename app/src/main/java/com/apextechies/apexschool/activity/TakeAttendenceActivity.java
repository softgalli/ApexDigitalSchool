package com.apextechies.apexschool.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.apextechies.apexschool.R;
import com.apextechies.apexschool.WeekCalendarFragment;
import com.apextechies.apexschool.adapter.AttendenceAdapter;
import com.apextechies.apexschool.calender.CsvFileWriter;
import com.apextechies.apexschool.calender.RealMController;
import com.apextechies.apexschool.listener.CalenderListener;
import com.apextechies.apexschool.model.Student;
import com.apextechies.apexschool.preference.Prefs;
import com.apextechies.apexschool.retrofit.RetrofitDataProvider;
import com.apextechies.apexschool.utils.ViewUtils;
import com.apextechies.apexschool.utils.WeekCalendarOptions;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class TakeAttendenceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final int REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 301;
    private static final String TAG = TakeAttendenceActivity.class.getSimpleName();
    public static String[] mStrArrExternalStorageReadWritePermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.totalStudentCount)
    TextView totalStudentCount;
    @BindView(R.id.presentStudentCount)
    TextView presentStudentCount;
    @BindView(R.id.absentStudentCount)
    TextView absentStudentCount;
    @BindView(R.id.filePath)
    TextView filePath;
    @BindView(R.id.uploadAttendenceBtn)
    TextView uploadAttendenceBtn;
    @BindView(R.id.seeAttendenceBtn)
    TextView seeAttendenceBtn;
    @BindView(R.id.mDateSelectedTv)
    TextView mDateSelectedTv;
    private WeekCalendarFragment mWeekCalendarFragment;
    private String fileName = "";
    private Activity mActivity;
    private Realm realm;
    private AttendenceAdapter mAdapter;
    private ArrayList<Student> studentList;
    private String className = "";
    public boolean isAttendenceTakenAndSaved = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance_activity);
        ButterKnife.bind(this);
        mActivity = this;
        mWeekCalendarFragment = (WeekCalendarFragment) getSupportFragmentManager()
                .findFragmentByTag(WeekCalendarFragment.class.getSimpleName());

        manageWeekCalenderView();

        initView();

        initReam();

        getStudentListFromServer();

        manageStudentList();

        manageCreateAndUploadAttendence();

        checkPermissionForReadStorage();

    }


    private void initReam() {
        //get realm instance
        this.realm = RealMController.with(this).getRealm();

        // refresh the realm instance
        RealMController.with(this).refresh();
    }

    private void initView() {
        mActivity = this;
        studentList = new ArrayList();

        if (getIntent().getExtras() != null && getIntent().hasExtra("ClassName"))
            className = getIntent().getStringExtra("ClassName");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Take Attendence Class " + className);

        mRecyclerView.setNestedScrollingEnabled(false);
    }

    private void manageCreateAndUploadAttendence() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/Attendence");
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        boolean var = false;
        if (!folder.exists())
            var = folder.mkdir();

        Log.i(TAG, "var : " + var);

        fileName = folder.toString() + "/Class_" + className + "_" + formattedDate + ".csv";
        filePath.setText(fileName);
    }

    private RetrofitDataProvider retrofitDataProvider;

    private void getStudentListFromServer() {
        retrofitDataProvider = new RetrofitDataProvider(mActivity);
        //Use Here your common internet checker method
        if (ViewUtils.isOnline(mActivity)) {
            /*retrofitDataProvider.getStudentListForAttendence("apexschool_1001", new DownlodableCallback<Student>() {
                @Override
                public void onSuccess(final Student result) {
                    //  closeDialog();
                    if (result.getStatus().contains(PreferenceName.TRUE)) {
                        studentList = result.getData();
                    }
                    if (studentList != null && studentList.size() > 0)
                        getStudentListFromRealM();
                }

                @Override
                public void onFailure(String error) {
                    // closeDialog();
                }

                @Override
                public void onUnauthorized(int errorNumber) {

                }
            });*/


            /*###########################################################################################*/
            //TODO remove after calling api and getting response from server
            for (int i = 1; i <= 15; i++) {
                Student st = new Student("SchoolName" + i, "Student Name " + i, "Father Name " + i, "F", 21);
                studentList.add(st);
            }
            getStudentListFromRealM();
            /*###########################################################################################*/

        } else if (Prefs.isPreLoaded()) {
            RealMController realMController = RealMController.with(mActivity);
            if (realMController != null)
                studentList.addAll(realMController.getStudentsList());
        } else {
            ViewUtils.showNoInternetConnectionDialog(mActivity);
        }
    }

    private void getStudentListFromRealM() {
        //saving loaded data from server to realm
        if (studentList != null && studentList.size() > 0) {
            for (Student student : studentList) {
                // Persist your data easily
                realm.beginTransaction();
                realm.copyToRealm(student);
                realm.commitTransaction();
            }

            Prefs.setPreLoad(true);
        }
    }

    private void manageStudentList() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mAdapter = new AttendenceAdapter(studentList, mActivity);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

    }

    public void seeAttendence() {
        try {
            List<Student> studentsList = (mAdapter).getStudentist();
            if (!TextUtils.isEmpty(fileName) && studentsList != null && studentsList.size() > 0) {
                CsvFileWriter.writeCsvFile(fileName, studentsList);
            } else {
                Toast.makeText(mActivity, "Something went wrong while creating attendence csv file", Toast.LENGTH_SHORT).show();
            }
            showAttendence();
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong while creating attendence csv file,\nReason : " + e);
        }
    }

    public void showAttendence() {
        // Create URI
        File file = new File(fileName);
        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/vnd.ms-excel");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(pdfIntent);
            } catch (Exception e) {
                ViewUtils.showMessageOnDialog(mActivity, "Install MS-Excel", "Please install MS-Excel app to view the file.");
            }
        }
    }

    /*******************************************************************************
     * Method Name : checkPermissionForReadStorage
     * Description : This method will request  Permission  for read and Write Storage
     */
    public void checkPermissionForReadStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, mStrArrExternalStorageReadWritePermissions,
                    REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (showRationale) {
                            //For simple deny permission
                            //showPopupForCameraPermission();
                        } else if (!showRationale) {
                            // for NEVER ASK AGAIN deny permission
                            showPopupForCameraPermission();
                        }
                    }
                }
            }
        }
    }

    /**
     * Method Name : showPopupForCameraPermission
     * Description : Method used to show popup for camera permission
     */
    private void showPopupForCameraPermission() {
        Toast.makeText(mActivity, "Need storage permission, please enable it from settings.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    /*###############################################################################################################################################################################
    ###############################################################################################################################################################################
    ###############################################################################################################################################################################*/


    private void manageWeekCalenderView() {
        if (mWeekCalendarFragment == null) {
            mWeekCalendarFragment = new WeekCalendarFragment();

            Bundle args = new Bundle();

           /* Must add this attribute if using ARGUMENT_NOW_BACKGROUND or ARGUMENT_SELECTED_DATE_BACKGROUND*/
            args.putString(WeekCalendarFragment.ARGUMENT_PACKAGE_NAME
                    , getApplicationContext().getPackageName());

            // Sets color to the primary views (Month name and dates)
            args.putInt(WeekCalendarFragment.ARGUMENT_PRIMARY_TEXT_COLOR
                    , ContextCompat.getColor(this, R.color.colorTextPrimary));

            // Picks between three or one date header letters ex. "Sun" or "S"
            // two options:
            // 1. WeekCalendarOptions.DAY_HEADER_LENGTH_THREE_LETTERS
            // 2. WeekCalendarOptions.DAY_HEADER_LENGTH_ONE_LETTER
            args.putString(WeekCalendarFragment.ARGUMENT_DAY_HEADER_LENGTH
                    , WeekCalendarOptions.DAY_HEADER_LENGTH_ONE_LETTER);

            // Days that have events
            ArrayList<Calendar> eventDays = new ArrayList<>();
            eventDays.add(Calendar.getInstance());
            long[] eventLong = new long[1000];
            for (int i = 0; i < eventDays.size(); i++) {
                eventLong[i] = eventDays.get(i).getTimeInMillis();
            }
            args.putSerializable(WeekCalendarFragment.ARGUMENT_EVENT_DAYS, eventLong);

            mWeekCalendarFragment.setArguments(args);

            // Attach to the activity
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.container, mWeekCalendarFragment, WeekCalendarFragment.class.getSimpleName());
            t.commit();
        }

        CalenderListener listener = new CalenderListener() {
            @Override
            public void onSelectPicker() {
                //User can use any type of pickers here the below picker is only Just a example
                DatePickerDialog.newInstance(TakeAttendenceActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                        .show(getFragmentManager(), "datePicker");
            }

            @Override
            public void onSelectDate(LocalDateTime mSelectedDate) {
                int sunday = mSelectedDate.getDayOfWeek();
                if (sunday == Calendar.SATURDAY) {
                    mDateSelectedTv.setText("Its Sunday !!");
                } else {
                    //callback when a date is selcted
                    mDateSelectedTv.setText(""
                            + mSelectedDate.getDayOfMonth()
                            + "-"
                            + mSelectedDate.getMonthOfYear()
                            + "-"
                            + mSelectedDate.getYear());
                }
            }
        };
        //setting the listener
        mWeekCalendarFragment.setCalenderListener(listener);

        mWeekCalendarFragment.setPreSelectedDate(Calendar.getInstance());
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        //This is the call back from picker used in the sample. You can use custom or any other picker
        //IMPORTANT: get the year,month and date from picker you using and call setDateWeek method
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        mWeekCalendarFragment.setDateWeek(calendar);//Sets the selected date from Picker
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                saveAttendenceBeforeLeave();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAttendenceBeforeLeave();
    }

    private void saveAttendenceBeforeLeave() {
        if (isAttendenceTakenAndSaved) {
            Toast.makeText(mActivity, "Attendence Taken", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(mActivity, "Are you sure?", Toast.LENGTH_SHORT).show();

    }

    @OnClick({R.id.uploadAttendenceBtn, R.id.seeAttendenceBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.uploadAttendenceBtn:
                //uploadAttendence();
                break;
            case R.id.seeAttendenceBtn:
                seeAttendence();
                break;
        }
    }

    public void manageAbsentPresentCount(int mIntTotalStudentCount, int mIntAbsentStudentCount) {
        if (totalStudentCount != null)
            totalStudentCount.setText(mIntTotalStudentCount + "\nTotal Students");
        if (presentStudentCount != null)
            presentStudentCount.setText((mIntTotalStudentCount - mIntAbsentStudentCount) + "\nPresent");
        if (absentStudentCount != null)
            absentStudentCount.setText(mIntAbsentStudentCount + "\nAbsent");
    }
}
