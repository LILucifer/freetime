import com.weixiao.smart.SpringBootApplication;
import com.weixiao.smart.poi.ExcelUtil;
import com.weixiao.smart.poi.entity.TestImprotExcelModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/8/7 10:15
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootApplication.class)
public class PoiTest {

    public static Map<Class, Map<String, String>> importPrjSeMark() {
        Map<Class, Map<String, String>> businessMap = new HashMap<Class, Map<String, String>>();
        //PrjCcSettlement主表
        Map<String, String> map = new HashMap<String, String>();
        map.put("operateUnits", "1");
        map.put("partnerCode", "2");
        map.put("projectCode", "3");
        map.put("amount", "4");
        map.put("month", "5");
        businessMap.put(TestImprotExcelModel.class, map);
        return businessMap;
    }

    @Test
    public void testImportExcel() {
//        String filePath2 = "D:\\Users\\Lucifer-Wi\\ChromeDownload\\项目合作费（丙类）月度结算表-导入模板 (2).xls";
//        try {
//            FileInputStream fio = new FileInputStream(filePath);
//            Map<String, Map<String, Object>> datas = ExcelUtil.readExcelContent(fio, 1, 2, 5, importPrjSeMark());
//            log.info("importDate = {}" , datas);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String filePath2 = "D:\\Users\\Lucifer-Wi\\ChromeDownload\\项目合作费（丙类）月度结算表-导入模板 (4).xlsx";
        try {
            FileInputStream fio = new FileInputStream(filePath2);
            Map<String, Map<String, Object>> datas = ExcelUtil.readExcelContentXSS(fio, 1, 2, 5, importPrjSeMark());
            log.info("importDate = {}" , datas);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
