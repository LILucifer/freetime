package com.weixiao.smart.download.util;


import java.io.File;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-05-16 21:07.
 */
public class FileOperate {
    public int count = 0;

    public void deleteFile(String filepath) {
        File file = new File(filepath);
        deleteFile(file);
    }

    public void deleteFile(File file) {
        count++;
        if (file.isDirectory()) {//文件夹操作
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    deleteFile(f);
                }
            }
            file.delete();
        }
        if (file.isFile()) {//文件直接删除
            file.delete();
        }
    }
}
