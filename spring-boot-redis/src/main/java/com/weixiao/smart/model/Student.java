package com.weixiao.smart.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/2/20 17:52
 */
@Data
public class Student implements Serializable{
    private String studentId;
    private String name;
    private int age;
    private String information;

    public Student(String studentId, String name, int age, String information) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.information = information;
    }
}
