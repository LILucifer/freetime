import com.weixiao.smart.RedisDemoApplication;
import com.weixiao.smart.model.Student;
import com.weixiao.smart.redis.jedis.RedisUtils;
import com.weixiao.smart.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/2/25 10:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RedisDemoApplication.class})
@Slf4j
public class SpringCacheTest {
    @Autowired
    @Qualifier("stringKeyRedisTemplate")
    private RedisTemplate redisTemplate;
    @Autowired
    List<RedisTemplate> redisTemplates;
    @Autowired
    private StudentService studentService;

    private String studetId = "0000005";
    @Test
    public void getStudent() {
        //获取学生信息
        log.info("获取学生信息 {}",studentService.getStudent(studetId));
    }
    @Test
    public void updateStudent() {
        Student student = studentService.getStudent(studetId);
        student.setName("nike !");
        //更新学生信息
        log.info("更新学生信息{}",studentService.updateStudent(student));;
    }
    @Test
    public void deleteStudent() {
        //删除学生信息
        studentService.deleteStudent(studetId);
    }
    @Test
    public void getStudentInfoFromRedis() {
        String key = "spring:cache::"+studetId;
        Student student = (Student) redisTemplate.opsForValue().get(key);
        //Student student2 = RedisUtils.getObj(key, Student.class);

        log.info("get student info from redis  = {}", student);
    }
}
