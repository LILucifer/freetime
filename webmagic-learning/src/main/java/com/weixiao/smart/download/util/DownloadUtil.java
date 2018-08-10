package com.weixiao.smart.download.util;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author lishixiang@echinetech.com.
 * @description (下载工具类)
 * @Created 2018-05-13 21:31.
 */
public class DownloadUtil {
    private static String path = "/Users/apple_WeiXiao/Downloads/webmagic/58pic/";

    public DownloadUtil(String path) {
        if (path != null && path != "") {
            this.path = path;
        }
        chekDirs();
    }
    public void chekDirs() {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static Boolean downloadFile(String urlstr) {
        try {
            String filename = getImgName(urlstr);
            if (!new File(path + filename).exists()) {//先检查文件是否已下载，已下载则忽略
                URL url = new URL(urlstr);
                URLConnection con = url.openConnection();
                con.setConnectTimeout(10000);
                InputStream in = con.getInputStream();
                FileOutputStream out = new FileOutputStream(path + filename);
                BufferedOutputStream bout = new BufferedOutputStream(out);
                byte[] b = new byte[2048];
                int len;
                while ((len = in.read(b)) != -1) {
                    bout.write(b,0,len);
                }
                bout.close();
                out.close();
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true ;
    }
    public static Boolean downloadFile(String urlstr,String filename) {
        try {
            if (!new File(path + filename).exists()) {//先检查文件是否已下载，已下载则忽略
                URL url = new URL(urlstr);
                URLConnection con = url.openConnection();
                con.setConnectTimeout(6000);
                InputStream in = con.getInputStream();
                FileOutputStream out = new FileOutputStream(path + filename);
                BufferedOutputStream bout = new BufferedOutputStream(out);
                byte[] b = new byte[2048];
                int len;
                while ((len = in.read(b)) != -1) {
                    bout.write(b,0,len);
                }
                bout.close();
                out.close();
                in.close();
            }

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
