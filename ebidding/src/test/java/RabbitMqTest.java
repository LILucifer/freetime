import com.weixiao.smart.ebidding.Application;
import com.weixiao.smart.ebidding.rabbitmq.Producer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-03-30 23:35.
 */
@Slf4j
@Data
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RabbitMqTest {

    @Autowired
    private Producer producer;
    @Test
    public  void  rabbitMqTest(){
        for (int i = 0; i < 100; i++) {
            producer.sendMQ("rabbiMq message info "+ i);
        }

        ReentrantLock reentrantLock;
        try {
            Thread.sleep(1000*60*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
