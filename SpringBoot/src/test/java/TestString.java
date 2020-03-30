import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-07-18 08:00.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TestString {
    public void buiderString(){
        String a = "ss";
                a+= "s" ;
        a += "sd";
        a += "sjdk";
        StringBuilder b = new StringBuilder("jdhfjjdsf").append("sdasd");
        String c = b.toString();
    }
    @Test
    public void testSubstring(){
        String path = "/manage/NoticeInfo/list";
        path = path.substring(0,path.lastIndexOf("/"));
        System.out.println(path);
    }
}
