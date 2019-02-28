package com.weixiao.smart.judger;

import com.weixiao.smart.container.ContainerUtils;
import com.weixiao.smart.entity.ProxyIpModel;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import static com.weixiao.smart.container.Constants.*;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-17 20:48.
 */
@Slf4j
public class Judgment {



    /**
     * 验证proxy ip 是否有效
     */
    public static boolean judgeStrategy(ProxyIpModel model) {
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(model.getIp(), Integer.parseInt(model.getPort())));
            URL url = new URL(JUDGER_URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(proxy);
            connection.setConnectTimeout(TIME_OUT);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            InputStream inputStream = connection.getInputStream();
            return true;
        } catch (IOException e) {
            //log.info("{} proxy ip was useless ,and fail {} times", model ,model.getFailCount());
        }
        return false;
    }

    public static void JsoupResultVerify() {
        if (ContainerUtils.getJsoupResultMap().size() < 50) {
            ContainerUtils.getJsoupResultMap().forEach((key, value) -> {
                ProxyIpModel model = (ProxyIpModel) value;
                if (judgeStrategy(model)) {
                    ContainerUtils.addControllerMap((String) key , model);
                }
            });
        }
    }

}
