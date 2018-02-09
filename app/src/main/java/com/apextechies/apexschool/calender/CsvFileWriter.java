package com.apextechies.apexschool.calender;


import android.text.TextUtils;
import android.util.Log;

import com.apextechies.apexschool.model.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author ashraf
 */
public class CsvFileWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER = "Student Id,Student Name,Father's Name,Gender,Age,Attendance";
    private static final String TAG = CsvFileWriter.class.getSimpleName();

    public static void writeCsvFile(String fileName, List<Student> studentsList) {

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file
            for (int i = 0; i < studentsList.size(); i++) {
                if (i < studentsList.size()) {
                    Student student = studentsList.get(i);
                    if (student != null) {
                        if (!TextUtils.isEmpty(String.valueOf(student.getStudentId())))
                            fileWriter.append(String.valueOf(student.getStudentId()));
                        fileWriter.append(COMMA_DELIMITER);
                        if (student.getStudentName() != null && !TextUtils.isEmpty(student.getStudentName()))
                            fileWriter.append(student.getStudentName());
                        fileWriter.append(COMMA_DELIMITER);
                        if (student.getFatherName() != null && !TextUtils.isEmpty(student.getFatherName()))
                            fileWriter.append(student.getFatherName());
                        fileWriter.append(COMMA_DELIMITER);
                        if (student.getGender() != null && !TextUtils.isEmpty(student.getGender()))
                            fileWriter.append(student.getGender());
                        fileWriter.append(COMMA_DELIMITER);
                        if (!TextUtils.isEmpty(String.valueOf(student.getAge())))
                            fileWriter.append(String.valueOf(student.getAge()));
                        fileWriter.append(COMMA_DELIMITER);
                        if (!TextUtils.isEmpty(String.valueOf(student.isSelected())))
                            fileWriter.append(String.valueOf(student.isSelected()));
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    }
                }
            }

            Log.i(TAG, "CSV file was created successfully !!!");

        } catch (Exception e) {
            Log.e(TAG, "Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }
}