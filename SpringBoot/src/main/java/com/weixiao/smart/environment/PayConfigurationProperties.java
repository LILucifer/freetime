package com.weixiao.smart.environment;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-11-30 17:36.
 */
@Component
@Data
@Slf4j
@ConfigurationProperties(prefix="pay.configuration")
public class PayConfigurationProperties implements ConfigurationCenterUpdate {

    /**
     * 支付宝地址
     */
    //@Value("${pay.configuration.aliPayUrl}")
    private String aliPayUrl;
    /**
     * 微信支付地址
     */
    //@Value("${pay.configuration.weChatPayUrl}")
    private String weChatPayUrl;
    /**
     * 银联支付地址
     */
    //@Value("${pay.configuration.bankPayUrl}")
    private String bankPayUrl;

    @Override
    public void updateProperties(String configName, String newValue) {
        try {
            String fieldName = configName.replace(getBasePath(), "");
            if (fieldName != null && fieldName != "") {
                ReflectUtils.setValue(this, this.getClass(), fieldName, this.getClass().getDeclaredField(fieldName).getType(), newValue);
                log.info("key: {} value: {} data  update successful " , getBasePath()+fieldName , newValue);
                log.info(toString());
            }
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
        }catch (Exception e){

        }
    }

    @Override
    public String getBasePath() {
        return "/zk_config/pay.configuration.";
    }

    @Override
    public String toString() {
        return "PayConfigurationProperties{" +
                "aliPayUrl='" + aliPayUrl + '\'' +
                ", weChatPayUrl='" + weChatPayUrl + '\'' +
                ", bankPayUrl='" + bankPayUrl + '\'' +
                '}';
    }
}
