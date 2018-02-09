package com.apextechies.apexschool.model;

import java.io.Serializable;

import io.realm.RealmObject;

public class Student extends RealmObject implements Serializable {
    private String studentName;
    private String studentId;
    private String fatherName;
    private String gender;
    private boolean isSelected;
    private int age;

    public Student() {
    }

    public Student(String studentId, String studentName, String fatherName, String gender, int age) {

        this.studentName = studentName;
        this.studentId = studentId;
        this.fatherName = fatherName;
        this.gender = gender;
        this.age = age;
        isSelected = false;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}