package com.weixiao.smart.service;

import com.weixiao.smart.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/2/25 8:59
 */
@Service
@Slf4j
public class StudentService {

    // value~单独的缓存前缀
    // key缓存key 可以用springEL表达式
    @Cacheable(cacheManager = "cacheManager" , value = "spring:cache" , key = "#studentId")
    public Student getStudent(String studentId) {
        //redis key = ${value}::${key}   --- spring:cache::#studentId
        log.info("从数据库获取{} 学生信息", studentId);
        return new Student(studentId,"tom",35,"tom infomation test");
    }

   @CacheEvict(cacheManager = "cacheManager", value = "spring:cache", key = "#studentId")
    public void deleteStudent(String studentId) {
       log.info("从数据库删除{} 学生信息成功，请检查缓存！", studentId);
    }

    @CachePut(cacheManager = "cacheManager", value = "spring:cache", key = "#student.studentId", condition = "#result ne null")
    public Student updateStudent(Student student) {
        log.info("数据库更新{} 学生信息，检查缓存是否一致！", student.getStudentId());
        return student;
    }

}
