package com.weixiao.smart.jdk8features.lambda;

import com.weixiao.smart.jdk8features.lambda.entity.Person;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.*;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-05 13:14.
 */
@Slf4j
public class LambdaUse {
    public static void test() throws SQLException {
        LambdaInterface lambdaInterface = (v) ->{
            log.info("UserName = {}" , v);
            return v;
        };
        Map<String, String> map = new HashMap();
        map.put("key1", "key1value");
        map.put("key2", "key2value");
        map.put("key3", "key3value");
        map.forEach((k,v)->{
            log.info("key = {} , value = {}", k, v);
            lambdaInterface.getUserName(v);

        });

        DeFaultInterface deFaultInterface = ()->{
            log.info("deFaultInterface");
        };
        deFaultInterface.getUserName();
        deFaultInterface.printUserId();
        deFaultInterface.getUserNo();
        throw new SQLException("test  SQLException ");
    }
    public static  void testOuter(){
        InstanceOuter io = new InstanceOuter(12);
        // Is this a compile error?
        InstanceOuter.InstanceInner ii = io.new InstanceInner();

        // What does this print?
        ii.printSomething(); // prints 12

        // What about this?
        StaticOuter.StaticInner si = new StaticOuter.StaticInner();
        si.printSomething(); // prints 24
        throw new RuntimeException("testOuter RuntimeException");
    }

    public static void testPersonSort() throws Exception {
        List<Person> people = Arrays.asList(
                new Person("Ted", "Neward", 42),
                new Person("Charlotte", "Neward", 39),
                new Person("Michael", "Neward", 19),
                new Person("Matthew", "Neward", 13),
                new Person("Neal", "Ford", 45),
                new Person("Candy", "Ford", 39),
                new Person("Jeff", "Brown", 43),
                new Person("Betsy", "Brown", 39)
        );
        Collections.sort(people , Person::compareLastAndAge);
        people.forEach(person -> {
            log.info("name = {} , lastName = {} ,  age = {}", person.getFirstName(),person.getLastName(), person.getAge());
        });
        people.stream().filter(person ->  person.getAge() ==39).forEach(person -> {
            log.info(" after stream name = {} , lastName = {} ,  age = {}", person.getFirstName(),person.getLastName(), person.getAge());
        });
        //throw new Exception("testPersonSort Exception");
        throw new Error("testPersonSort Error");
    }

    public static void main(String[] args) {
        try {
            test();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            testPersonSort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            testOuter();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
