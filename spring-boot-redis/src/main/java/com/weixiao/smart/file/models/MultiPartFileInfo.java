package com.weixiao.smart.file.models;

import lombok.Data;

/**
 * @author lishixiang
 * @Title:
 * @Description: 附件分块信息
 * @date 2020/5/1 16:49
 */
@Data
public class MultiPartFileInfo {

    //完整附件路径
    private String filePath;
    //分块MD5
    private String fileMd5;
    //分区附件名
    private String fileName;
    //分块起始位置
    private long off;
    //分块大小 单位字节(byte)
    private long len;
}
