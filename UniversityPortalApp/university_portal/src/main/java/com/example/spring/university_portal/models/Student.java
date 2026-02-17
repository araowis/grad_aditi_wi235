package com.example.spring.university_portal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "registration_number")
    private Integer regNo;  

    @Column(name = "roll_number")
    private Integer rollNo;               

    @Column(name = "student_name")
    private String name;

    private Integer standard;

    @Column(name = "school_name")
    private String schoolName;

    private String gender;

    private Double percentage;

    protected Student() {
    }

    public Student(Integer regNo, Integer rollNo, String name, Integer standard, String schoolName, String gender, Double percentage) {
        this.regNo = regNo;
        this.rollNo = rollNo;
        this.name = name;
        this.standard = standard;
        this.schoolName = schoolName;
        this.gender = gender;
        this.percentage = percentage;
    }

    public Integer getRegNo() {
        return regNo;
    }

    public void setRegNo(Integer regNo) {
        this.regNo = regNo;
    }

    public Integer getRollNo() {
        return rollNo;
    }

    public void setRollNo(Integer rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStandard() {
        return standard;
    }

    public void setStandard(Integer standard) {
        this.standard = standard;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "Student [regNo=" + regNo + ", rollNo=" + rollNo + ", name=" + name + ", standard=" + standard
                + ", schoolName=" + schoolName + ", gender=" + gender + ", percentage=" + percentage + "]";
    }

    
}