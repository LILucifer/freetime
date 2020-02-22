import com.weixiao.smart.RedisDemoApplication;
import com.weixiao.smart.model.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/2/20 15:47
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RedisDemoApplication.class})
public class RedisTemplateTest {


    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testRedisGetValue() {
        //key value
        String key = "testRedisGetValue";
        redisTemplate.opsForValue().set("testRedisGetValue", "this is test key ");
        log.info("{} = {}", key, redisTemplate.opsForValue().get(key));

        //List操作
        List<String> students = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            students.add("student" + i);
        }
        String studentKey = "studentKey";
        redisTemplate.opsForList().rightPushAll(studentKey, students);
        log.info("{} = {}", studentKey, redisTemplate.opsForList().range(studentKey, 0, -1));
    }

    @Test
    public void testJsonLength(){
        MessageEntity entity = new MessageEntity();
        log.info("{}  length = {} 字节" ,entity.toString() , entity.toString().length());
    }
}
