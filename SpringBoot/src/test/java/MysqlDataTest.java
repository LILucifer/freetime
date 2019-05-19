import com.weixiao.smart.SpringBootApplication;
import com.weixiao.smart.jdbc.MysqlData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-07 17:13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SpringBootApplication.class})
public class MysqlDataTest {
    @Autowired
    private MysqlData mysqlData;

    @Test
    public void test(){
        try {
            mysqlData.getUserList();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
