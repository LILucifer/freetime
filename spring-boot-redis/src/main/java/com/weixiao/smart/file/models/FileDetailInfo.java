package com.weixiao.smart.file.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishixiang
 * @Title:
 * @Description: 附件信息
 * @date 2020/5/1 16:48
 */
@Data
public class FileDetailInfo {
    //附件ID
    private String fileId;
    //附件路径
    private String filePath;
    //附件名
    private String fileName;
    //附件MD5
    private String fileMd5;
    //文件大小
    private long size;
    //附件是否分块
    private boolean isMultipart;
    //附件分块信息
    List<MultiPartFileInfo> multiPartFileInfos;

    public void addMultiPart(MultiPartFileInfo partFileInfo) {
        if (multiPartFileInfos == null) {
            multiPartFileInfos = new ArrayList<>();
        }
        multiPartFileInfos.add(partFileInfo);
    }
}
