package com.weixiao.smart.jsoup;

import com.weixiao.smart.entity.ProxyIpModel;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import static com.weixiao.smart.container.Constants.TIME_OUT;
import static com.weixiao.smart.container.Constants.USER_AGENT;
import static com.weixiao.smart.container.ContainerUtils.getControllerMapProxyIpModelByRandom;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-01-16 16:12.
 */
@Slf4j
public class JsoupDoument {


    public static Document getDocument(String urlStr) throws IOException {
        ProxyIpModel model = getControllerMapProxyIpModelByRandom();
        Document doc;
        if (model != null) {
            String html = getHtmlStringByProxyUrl(urlStr, model.getIp(), Integer.parseInt(model.getPort()));
            doc = Jsoup.parse(html);
            return doc;
        }
        doc = Jsoup.connect(urlStr)
                .userAgent(USER_AGENT)
                .timeout(TIME_OUT).get();
        return doc;
    }

    private static String getHtmlStringByProxyUrl(String urlStr, String ip, int port) throws IOException {
        log.info("this proxy id = {} :{}", ip, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        URL url = new URL(urlStr);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(proxy);
        connection.setConnectTimeout(TIME_OUT);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        InputStream inputStream = connection.getInputStream();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer bs = new StringBuffer();
        String l = null;
        while ((l = buffer.readLine()) != null) {
            bs.append(l);
        }

        return bs.toString();

    }


}
