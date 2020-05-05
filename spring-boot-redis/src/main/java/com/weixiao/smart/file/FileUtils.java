package com.weixiao.smart.file;

import com.weixiao.smart.redis.jedis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.security.MessageDigest;

import org.assertj.core.api.Assertions;
import org.springframework.util.StreamUtils;

/**
 * @author lishixiang
 * @Title:
 * @Description: 文件操作工具类
 * @date 2020/5/1 16:47
 */
@Slf4j
public class FileUtils {
    private static final String MD5_ALGORITHM_NAME = "MD5";
    private static final char[] HEX_CHARS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 获取附件Md5
     *
     * @param path 本地附件路径
     * @return
     */
    public static String getFileMd5(String path) {
        String md5 = "";
        try {
            long starttime = System.currentTimeMillis();
            //先从缓存中查询MD5
            md5 = RedisUtils.get(path);
            if ("".equals(md5)||md5==null) {
                md5 = DigestUtils.md5DigestAsHex(new FileInputStream(path));
                RedisUtils.set(path, md5, 0);
            }
            log.info("获取附件 {} md5值 耗时={} ms", path, System.currentTimeMillis() - starttime);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取附件{} md5值失败，请检查！", path, e);
        }
        return md5;
    }

    /**
     * 获取输入流中数据MD5
     *
     * @param inputStream
     * @return
     */
    public static String getFileMd5(InputStream inputStream) {
        String md5 = "";
        try {
            long starttime = System.currentTimeMillis();
            md5 = DigestUtils.md5DigestAsHex(inputStream);
            log.info("获取附件md5值 耗时={} ms md5={}", System.currentTimeMillis() - starttime, md5);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取附件md5值失败，请检查！", e);
        }
        return md5;
    }

    /**
     * 获取输入流中数据MD5
     * @param file
     * @param off 起始位置
     * @param len 长度
     * @return
     */
    public static String getFileMd5( File file, String key, long off , long len) {
        String md5 = "";
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            long starttime = System.currentTimeMillis();
            md5 = RedisUtils.get(md5);
            if ("".equals(md5)||md5==null)  {
                randomAccessFile.seek(off);
                MessageDigest messageDigest = MessageDigest.getInstance(MD5_ALGORITHM_NAME);
                byte[] buffer = new byte[StreamUtils.BUFFER_SIZE];
                int bytesRead = -1;
                //预防读取超出分块范围大小
                long readContentLen = 0;
                if ((readContentLen + StreamUtils.BUFFER_SIZE) > len) {
                    buffer = new byte[Math.toIntExact(len - readContentLen)];
                }
                while ((bytesRead = randomAccessFile.read(buffer)) != -1) {
                    messageDigest.update(buffer, 0, bytesRead);
                }
                md5 = new String(encodeHex(messageDigest.digest()));
                randomAccessFile.close();
                RedisUtils.set(key, md5, 0);
            }
            log.info("获取附件md5值 耗时={} ms md5={}", System.currentTimeMillis() - starttime, md5);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取附件md5值失败，请检查！", e);
        }
        return md5;
    }
    private static char[] encodeHex(byte[] bytes) {
        char[] chars = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }
    /**
     * 判断磁盘空间是否充足
     * @param path
     * @param fileSize
     * @return
     */
    public static boolean enoughFreeSpace(String path , long fileSize) {
        File file = new File(path);
        if (file.isDirectory()) {
            return file.getFreeSpace() - fileSize > 0;
        }
        return false;
    }

    /**
     * 校验附件
     * @param downloadFilePath
     * @param fileMd5 源文件MD5
     * @param originalFileSize 源文件大小
     * @return true---两附件MD5值一样，false---两附件MD5值不一样
     */
    public  static boolean checkFile(String downloadFilePath ,String fileMd5 , long originalFileSize  ){
        try {
            File file = new File(downloadFilePath);
            Assertions.assertThat(fileMd5).isEqualToIgnoringCase(FileUtils.getFileMd5(new FileInputStream(file)));
            Assertions.assertThat(file.length()).isLessThanOrEqualTo(originalFileSize);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
