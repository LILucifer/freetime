package com.weixiao.smart.jdk8features.lambda.entity;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-06 14:36.
 */
public class Person {
    private String firstName;
    private String lastName;
    private int age;
    public Person(String fn, String ln, int a) {
        this.firstName = fn; this.lastName = ln; this.age = a;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public static int compareLastAndAge(Person lhs, Person rhs) {
        if (lhs.lastName.equals(rhs.lastName))
            return lhs.age - rhs.age;
        else
            return lhs.lastName.compareTo(rhs.lastName);
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
