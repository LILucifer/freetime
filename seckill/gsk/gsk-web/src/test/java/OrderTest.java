
import com.weixiao.smart.model.OrderMessage;
import com.weixiao.smart.service.IOrderService;
import org.apache.commons.beanutils.BeanMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lishixiang0925@126.com.
 * @description (junit测试用例)
 * @Created 2018-09-06 21:02.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-context.xml"})
public class OrderTest {

    @Autowired
    private IOrderService orderService;
    private String commodityId = "goodsId";
    @Test
    public void initStocks() {

        int stocks = 50;
        orderService.initStocks(commodityId, stocks, 30 * 60);

    }

    @Test
    public void testOrder(){//模拟秒杀活动
        for(int i=0;i<1000;i++) {
            final int  k =i;
            new Thread(new Runnable() {
                public void run() {
                    OrderMessage orderMessage = new OrderMessage();
                    orderMessage.setCommodityId(commodityId);
                    orderMessage.setUserId("customer--" + k);
                    orderMessage.setCount(3);
                    orderService.getOrder(new BeanMap(orderMessage));
                }
            }).start();
        }

    }

}
