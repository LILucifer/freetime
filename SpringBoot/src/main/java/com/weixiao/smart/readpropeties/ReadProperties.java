package com.weixiao.smart.readpropeties;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/1/8 14:42
 */
@Slf4j
public class ReadProperties {

    public static void main(String[] args) {
        Properties properties = CongfigurationUtil.getconfigs();
        String basePathBuilderImpl = properties.getProperty("config.basePathBuilderImpl");
        log.info(basePathBuilderImpl);
    }
}
