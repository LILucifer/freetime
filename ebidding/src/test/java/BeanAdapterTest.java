import com.weixiao.smart.ebidding.Application;
import com.weixiao.smart.ebidding.adapter.BeanAdapter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/3/11 11:05
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BeanAdapterTest {
    @Autowired
    private BeanAdapter beanAdapter;

    @Test
    public void testPayAdapter() {
        String WXpayResult = beanAdapter.beanAdapter("WX").pay();
        log.info(WXpayResult);
        String ZXpayResult = beanAdapter.beanAdapter("ZX").pay();
        log.info(ZXpayResult);
        String ZsspayResult = beanAdapter.beanAdapter("Zss").pay();
        log.info(ZsspayResult);

    }
}
