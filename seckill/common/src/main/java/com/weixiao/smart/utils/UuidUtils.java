package com.weixiao.smart.utils;

import java.util.UUID;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(生成唯一的UUID)
 * @date 2018/9/8 12:16
 */
public class UuidUtils {
    public static final String getGuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
