package com.weixiao.smart.readpropeties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class CongfigurationUtil {

    private static Properties properties;

    public static Properties getconfigs() {
        if (properties != null)
            return properties;
        ClassPathResource classPathResource = new ClassPathResource("ckfinder.conf");
        Properties properties = new Properties();
        try {
            properties.load(classPathResource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
