import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-04-09 23:35.
 */
@Slf4j
@RunWith(SpringRunner.class)
public class JavaBaseTest {
    @Test
    public void test1() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key","d1");
        map.put("key2", "d2");
        map.put("key3", "d3");
        map.put("key4", "d4");
        log.info("LinkHashMap = {}", map.toString());

        Map<String, Object> map2 = new HashMap<>();
        map2.put("key","d1");
        map2.put("key2", "d2");
        map2.put("key3", "d3");
        map2.put("key4", "d4");
        log.info("HashMap = {}", map2.toString());


    }
}
