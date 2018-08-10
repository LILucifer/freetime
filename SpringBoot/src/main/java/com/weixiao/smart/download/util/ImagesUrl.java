package com.weixiao.smart.download.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lishixiang@echinetech.com.
 * @description (读取img 的URL)
 * @Created 2018-05-13 18:50.
 */
public class ImagesUrl {

    private ImagesUrl(){
        fileRead();
    }
    private volatile static ImagesUrl imagesUrl;
    public static ImagesUrl getInstance() {
        if (imagesUrl != null) {
        }else{
            synchronized (ImagesUrl.class) {
                if (imagesUrl == null) {
                    imagesUrl = new ImagesUrl();
                }
            }
        }
        return imagesUrl;
    }


    private List<String> imgUrls = new ArrayList<String>();

    public List<String> getImgUrl() {
        return imgUrls;
    }

    public void setImgUrl(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public void fileRead() {
        String fileUrl = "/Users/apple_WeiXiao/Downloads/pictureUrl.txt" ;
        try {
            FileInputStream fin = new FileInputStream(fileUrl);
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String url = "";
            while( (url = bufferedReader.readLine()) != null){
                imgUrls.add(url);
            }
            fin.close();
            isr.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
