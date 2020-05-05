package com.weixiao.smart.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

/**
 * @author lishixiang
 * @Title:
 * @Description: 附件操作配置参数
 * @date 2020/5/1 17:21
 */
@Component
@Data
@ConfigurationProperties(prefix = "file.operation")
public class FileOperationProperties {
    private static int defaultMultipartSizeLimit = 104857600;
    private static int defaultMultipartSize = defaultMultipartSizeLimit;
    //附件分块阈值 单位byte
    private long multipartSizeLimit;
    //分块大小 单位byte
    private long multipartSize;
    //读取缓存区大小
    private int readBufLenSize;
    //附件下载地址
    private String fileUploadUrlForJunitTest;
    //附件零时目录
    private String tempDirect;
    //分片下载的固定线程池线大小
    private int nThreads;
    //线程池队列大小
    private int threadPoolQueueCapacity;

    public long getMultipartSizeLimit() {
        if (multipartSizeLimit <= 0) {
            return defaultMultipartSizeLimit;
        }
        return multipartSizeLimit;
    }

    public long getMultipartSize() {
        if (multipartSize <= 0) {
            return defaultMultipartSize;
        }
        return multipartSize;
    }

    public int getReadBufLenSize() {
        if (readBufLenSize <= 0) {
            return StreamUtils.BUFFER_SIZE;
        }
        return readBufLenSize;
    }
}
