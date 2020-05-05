package com.weixiao.smart.nio;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/4/22 17:13
 */
public class FileContentSort {
    public static void main(String[] args) {
        String sourceFile = "D:\\Users\\Lucifer-Wi\\Desktop\\mergeLog_ztb_V1.13_20200420.txt";
        String targetFile = "D:\\Users\\Lucifer-Wi\\Desktop\\ztb_V1.13_20200420.txt";
        try {
            FileInputStream inputStream = new FileInputStream(sourceFile);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Set<String> content = new TreeSet<>();
            String value = null;
            while ( (value = bufferedReader.readLine())!=null) {
                content.add(value);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

            //输出到新的文件中
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (String value2 : content) {
                bufferedWriter.write(value2);
                String newLine = System.getProperty("line.separator");
                bufferedWriter.write(newLine);
            }
            bufferedWriter.close();
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
