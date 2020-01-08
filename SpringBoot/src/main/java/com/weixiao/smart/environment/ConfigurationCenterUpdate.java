package com.weixiao.smart.environment;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-11-30 17:35.
 */
public interface ConfigurationCenterUpdate {
    /**
     * 更新Bean属性值
     *
     * @param configName zookeeper path
     * @param newValue   变更后的值
     */
    void updateProperties(String configName, String newValue);

    /***
     * 配置属性前缀
     * @return
     */
    String getBasePath();
}
