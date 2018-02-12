package com.apextechies.apexschool.retrofit;

import com.apextechies.apexschool.model.NotificationModel;
import com.apextechies.apexschool.model.Student;

/**
 * Created by Shankar on 1/27/2018.
 */

public interface ServiceMethods {
    void notification(String school_id, DownlodableCallback<NotificationModel> callback);
    void getStudentListForAttendence(String school_id, DownlodableCallback<Student> callback);
}
