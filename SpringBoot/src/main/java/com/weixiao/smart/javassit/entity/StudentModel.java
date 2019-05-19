package com.weixiao.smart.javassit.entity;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-09 14:41.
 */
public class StudentModel {
    private String studentName;
    private String studentNo;
    private int age;
    private int high;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNo() {
        return studentNo;
    }

//    public void setStudentNo(String studentNo) {
//        this.studentNo = studentNo;
//    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }
}
