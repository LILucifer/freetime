import com.weixiao.smart.utils.JedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(Redis Junit测试)
 * @date 2018/9/7 14:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-context.xml"})
public class RedisTest {

    @Test
    public  void test(){
        String key = "testkey";
        JedisUtil.set(key, "testvalue", 30*10);
        System.out.println("jedis set value success!----");
        String value = JedisUtil.get(key);
        System.out.println("jedis get value success! " + value);
    }
}
