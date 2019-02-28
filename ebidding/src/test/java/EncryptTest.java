import com.weixiao.smart.ebidding.Application;
import com.weixiao.smart.ebidding.encrypt.RSAEncrypt;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/2/28 11:18
 */
@Slf4j
@Data
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EncryptTest {
    @Value("${encrypt.isEncrypt}")
    private boolean isEncrypt;


    @Test
    public void testEncrypt() {
        String data = "{\"userId\":\"askd我\",\"userName\":\"tom\"}";

        try {
            Map<String, Object> keyMap = RSAEncrypt.genKeyPair();
            String publicKey = RSAEncrypt.getPublicKey(keyMap);
            String privateKey = RSAEncrypt.getPrivateKey(keyMap);
            log.info("publicKey : {}", publicKey);
            log.info("privateKey : {}", privateKey);
            log.info("target Encrypted data : {}", data);
            String afterEncryptData = RSAEncrypt.encryptedDataByPublicKey(data, publicKey);
            log.info("after encrypt data : {}", afterEncryptData);
            log.info("after decrypt data : {}",RSAEncrypt.decryptData(afterEncryptData , privateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testValue(){
        log.info("isEncrypt = {}" , isEncrypt );
    }
}
