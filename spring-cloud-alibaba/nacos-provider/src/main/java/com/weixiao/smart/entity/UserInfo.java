package com.weixiao.smart.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author lishixiang
 * @Title:
 * @Description:
 * @date 2021/6/5 12:04
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "service-provider.user-info")
public class UserInfo implements Serializable {
    private String userId;
    private String userName;

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
