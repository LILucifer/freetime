package com.weixiao.smart.file.models;

import com.weixiao.smart.file.models.MultiPartFileInfo;
import lombok.Data;

/**
 * @author lishixiang
 * @Title:
 * @Description: JunitTest 附件下载结果实体
 * @date 2020/5/3 17:27
 */
@Data
public class FileUploadResultModel {
    private String message;
    private boolean success;
    private MultiPartFileInfo multiPartFileInfo;
    private String localFilePath;
}
