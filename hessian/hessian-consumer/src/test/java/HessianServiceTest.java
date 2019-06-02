import com.weixiao.smart.Application;
import com.weixiao.smart.annotation.HessianReference;
import com.weixiao.smart.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-12-31 22:08.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class HessianServiceTest {
//    @Autowired
//    private IUserService userService;

    @HessianReference
    private IUserService userService;

    @Test
    public void testGetUserName(){
        log.warn(userService.getClass().toString());
        String result = userService.getUserName("t");
        System.out.println(result);
    }
}
