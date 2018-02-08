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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.apextechies.apexschool.R;
import com.apextechies.apexschool.adapter.AttendenceAdapter;
import com.apextechies.apexschool.model.Student;
import com.apextechies.apexschool.preference.Prefs;
import com.apextechies.apexschool.utilz.CsvFileWriter;
import com.apextechies.apexschool.utilz.RealMController;
import com.weekcalendar.listener.CalenderListener;
import com.weekcalendar.utils.WeekCalendarOptions;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

public class TakeAttendenceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = TakeAttendenceActivity.class.getSimpleName();
    private WeekCalendarFragment mWeekCalendarFragment;
    private TextView mDateSelectedTv;
    public static final int REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 301;
    public static final String PERMISSION_READ_EXT_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String PERMISSION_WRITE_EXT_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static String[] mStrArrExternalStorageReadWritePermissions = {PERMISSION_READ_EXT_STORAGE, PERMISSION_WRITE_EXT_STORAGE};
    private String fileName = "";
    private TextView filePath;
    private Activity mActivity;
    private Realm realm;
    private RecyclerView mRecyclerView;
    private AttendenceAdapter mAdapter;
    private ArrayList<Student> studentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance_activity);
        mActivity = this;
        mDateSelectedTv = (TextView) findViewById(R.id.txt_date);
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

        mRecyclerView = (RecyclerView) findViewById(R.id.studentsRecyclerView);

        filePath = (TextView) findViewById(R.id.filePath);
    }

    private void manageCreateAndUploadAttendence() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/Attendence");

        boolean var = false;
        if (!folder.exists())
            var = folder.mkdir();

        Log.i(TAG, "var : " + var);

        fileName = folder.toString() + "/" + "Class8.csv";
        filePath.setText(fileName);
    }

    private void getStudentListFromServer() {
        //Use Here your common internet checker method
        boolean internetConnection = true;
        if (internetConnection) {
            //TODO Need to get this list from API call
            for (int i = 1; i <= 15; i++) {
                Student st = new Student("SchoolName" + i, "Student Name " + i, "Father Name " + i, "F", 21);
                studentList.add(st);
            }
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
        } else if (Prefs.isPreLoaded()) {
            RealMController realMController = RealMController.with(mActivity);
            if (realMController != null)
                studentList.addAll(realMController.getStudentsList());
        }
    }

    private void manageStudentList() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mAdapter = new AttendenceAdapter(studentList);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

    }

    public void uploadAttendence(View view) {
        Toast.makeText(mActivity, "Uploading File", Toast.LENGTH_SHORT).show();

    }

    public void seeAttendence(View view) throws IOException {
        String data = "";
        List<Student> studentsList = ((AttendenceAdapter) mAdapter).getStudentist();
        for (int i = 0; i < studentsList.size(); i++) {
            Student singleStudent = studentsList.get(i);
            if (singleStudent.isSelected() == true) {
                Student student1 = new Student(singleStudent.getStudentId(), singleStudent.getStudentName(), singleStudent.getFatherName(), singleStudent.getGender(), singleStudent.getAge());
                data = data + "\n" + singleStudent.getStudentName().toString();
            }
        }
        Toast.makeText(mActivity, " " + data, Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(fileName) && studentsList != null && studentsList.size() > 0) {
            CsvFileWriter.writeCsvFile(fileName, studentsList);
        } else {
            Toast.makeText(mActivity, "", Toast.LENGTH_SHORT).show();
        }
        showAttendence();
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
                Toast.makeText(mActivity, "Please install MS-Excel app to view the file.",
                        Toast.LENGTH_SHORT).show();
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
                            showPopupForCameraPermission();
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
}
