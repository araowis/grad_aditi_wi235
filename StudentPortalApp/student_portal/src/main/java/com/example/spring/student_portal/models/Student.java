package com.example.spring.student_portal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "roll_no")
    private int rollNo;               

    @Column
    private String name;

    private int standard;

    @Column(name = "school_name")
    private String schoolName;

    protected Student() {
    }

    public Student(int rollNo, String name, int standard, String schoolName) {
        this.rollNo = rollNo;
        this.name = name;
        this.standard = standard;
        this.schoolName = schoolName;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Override
    public String toString() {
        return "Student [rollNo=" + rollNo + ", name=" + name + ", standard=" + standard + ", schoolName=" + schoolName + "]";
    }
}