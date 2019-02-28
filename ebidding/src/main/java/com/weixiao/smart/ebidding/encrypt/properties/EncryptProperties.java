package com.weixiao.smart.ebidding.encrypt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lishixiang0925@126.com.
 * @description (加密配置项)
 * @Created 2019-02-28 23:00.
 */
@Data
@ConfigurationProperties(prefix = "encrypt")
@Component
public class EncryptProperties {
    /**
     * 是否开启密钥岩前
     */
    private boolean isEncrypt = false;
    /**
     * RSA公钥
     */
    private String publicKey;
    /**
     * RSA密钥
     */
    private String privateKey;
}
