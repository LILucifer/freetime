import com.weixiao.smart.ebidding.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/7/22 16:20
 */
@Slf4j
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class JdkTest {
    @Test
    public void test1() {

        BigDecimal num1 = new BigDecimal("11.006").setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal num2 = new BigDecimal("11.002").setScale(2, BigDecimal.ROUND_HALF_UP);
        log.info("num1= {} , num2 = {}", num1, num2);

        BigDecimal num3 = new BigDecimal("11.006").setScale(2, BigDecimal.ROUND_UP);
        BigDecimal num4 = new BigDecimal("11.002").setScale(2, BigDecimal.ROUND_UP);
        log.info("num3= {} , num4 = {}", num3, num4);

        BigDecimal num5 = new BigDecimal("11.006").setScale(2, BigDecimal.ROUND_DOWN);
        BigDecimal num6 = new BigDecimal("11.002").setScale(2, BigDecimal.ROUND_DOWN);
        log.info("num5= {} , num6 = {}", num5, num6);


    }
}
