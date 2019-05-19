import com.weixiao.smart.entity.StudentEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static com.weixiao.smart.entity.StudentEnum.STUDENT_NO;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-04-14 19:59.
 */
@Slf4j
@RunWith(SpringRunner.class)
public class EnumDemoTest {
    enum Type {
        STUDENT{
            String getInfo(){
            return "this is time";
        }},CLASS{
            @Override
            String getInfo() {
                return null;
            }
        },CAR{
            @Override
            String getInfo() {
                return null;
            }
        };

        abstract String getInfo();
    }
    @Test
    public void test1(){
        Type.values();
        log.info("compare result = {}",Type.STUDENT.compareTo(Type.CLASS));
        log.info("STUDENT ENUM : {}", STUDENT_NO);
        int i = 2;
        StudentEnum studentEnum = STUDENT_NO;
        switch (studentEnum) {
            case STUDENT_NO:
                log.info(STUDENT_NO.toString());
        }
        log.info("value of = {}" , StudentEnum.valueOf("STUDENT_NO"));
        log.info("customized enum : {}", Type.STUDENT.getInfo());
    }
}
