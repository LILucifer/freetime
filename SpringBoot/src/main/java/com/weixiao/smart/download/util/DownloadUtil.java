package com.weixiao.smart.download.util;



import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * @author lishixiang@echinetech.com.
 * @description (下载工具类)
 * @Created 2018-05-13 21:31.
 */
public class DownloadUtil {
    private static String path = "/Users/apple_WeiXiao/Downloads/";

    public DownloadUtil(String path) {
        this.path = path;
    }

    public static Boolean downloadFile(String urlstr) {
        try {
            URL url = new URL(urlstr);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(7);
            InputStream in = con.getInputStream();
            String filename = getImgName(urlstr);
            FileOutputStream out = new FileOutputStream(path + filename);
            BufferedOutputStream bout = new BufferedOutputStream(out);
            byte[] b = new byte[2048];
            int len;
            while ((len = in.read(b)) != -1) {
                bout.write(b,0,len);
            }
            in.close();
            out.close();
            bout.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true ;
    }
    private static String getImgName(String imgUrl) {
        if (imgUrl == null) {
            return null;
        }
        String[] strs = imgUrl.split("/");
        return strs[strs.length - 1];
    }
}
