import com.weixiao.smart.service.amq.MessageProviderService;
import com.weixiao.smart.model.OrderMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lishixiang0925@126.com.
 * @description (activeMq junit)
 * @Created 2018-09-01 10:36.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-context.xml"})
public class AmqTest {
    @Autowired
    private MessageProviderService providerService;
    @Test
    public void sendTopicMessage(){
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setCommodityId("----");
        orderMessage.setUserId("me");
        orderMessage.setCount(3);
        providerService.send(orderMessage);
    }
}
