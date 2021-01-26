package com.weixiao.smart.feign.controller;

import com.weixiao.smart.configuration.properties.FileOperationProperties;
import com.weixiao.smart.file.FileUtils;
import com.weixiao.smart.file.models.FileDetailInfo;
import com.weixiao.smart.file.models.MultiPartFileInfo;
import com.weixiao.smart.model.response.Result;
import com.weixiao.smart.redis.jedis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author lishixiang
 * @Title:
 * @Description: 附件操作controller
 * @date 2020/4/11 21:53
 */
@Slf4j
@RestController
@RequestMapping("/fileOperation")
public class FileOperationController {
    @Autowired
    private FileOperationProperties fileOperationProperties;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file, String fileMd5) {
        Result result = new Result();
        int verifySizeResult = verifySize(fileMd5, file.getSize());
        //上传起始位置
        int off = 0;
        //1、判断附件是否已经上传过 --- 24小时内
        if (verifySizeResult > 0) {
            // 还未上传过
            result = fastDfsUpload(file, off, fileMd5);
        } else if (verifySizeResult < 0) {
            // 部分已上传
            off = Integer.valueOf(RedisUtils.get(fileMd5)) - 1;
            result = fastDfsUpload(file, off, fileMd5);
        } else {
            //已完成上传，直接返回成功结果
            result.setSuccess(true);
        }
        return result;
    }


    private Result fastDfsUpload(MultipartFile file, int off, String fileMd5) {
        int size = 1024 * 1024;
        int length = 0;
        Result result = new Result();
        byte[] bytes = new byte[size];
        try {
            InputStream inputStream = file.getInputStream();
            while ((length = inputStream.read(bytes, off, size)) > 0) {
                off += length;
                //上传至FastDFS(附件)

                //缓存记录上传的位置信息
                RedisUtils.set(fileMd5, off + "", 24 * 60 * 60);
            }
            result.setMessage("上传成功");
            result.setSuccess(false);
        } catch (IOException e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * @param fileMd5
     * @param length
     * @return 0 已完成上传 ， < 0 完成部分上传 ， > 0 未开始上传
     */
    int verifySize(String fileMd5, Long length) {
        String cacheValue = RedisUtils.get(fileMd5);
        if (cacheValue != null) {
            return Long.valueOf(cacheValue).compareTo(length);
        }
        return 1;

    }

    /**
     * 获取附件信息
     *
     * @return 附件详细信息
     */
    @RequestMapping("/getFileDetailInfo")
    public FileDetailInfo getFileDetailInfo(String fileId) {
        FileDetailInfo fileDetailInfo = new FileDetailInfo();
        //附件id 获取附件信息
        String path = getFilePath(fileId);
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            fileDetailInfo.setSize(file.length());
            fileDetailInfo.setFilePath(path);
            fileDetailInfo.setFileName(file.getName());
            fileDetailInfo.setFileMd5(FileUtils.getFileMd5(path));
            if (file.length() > fileOperationProperties.getMultipartSizeLimit()) {
                fileDetailInfo.setMultipart(true);
                //超过分块阈值限制 进行附件分块
                long alreadyPartLength = 0;
                int part = 0;
                while (alreadyPartLength<file.length()) {
                    //起始分块位置
                    long off = alreadyPartLength;
                    //已分块的长度
                    alreadyPartLength = (alreadyPartLength + fileOperationProperties.getMultipartSize()) > file.length() ?
                            file.length() : alreadyPartLength + fileOperationProperties.getMultipartSize();
                    MultiPartFileInfo multiPartFileInfo = new MultiPartFileInfo();
                    multiPartFileInfo.setFileName(file.getName()+part);
                    multiPartFileInfo.setLen(alreadyPartLength - off);
                    //分块的MD5
                    multiPartFileInfo.setFileMd5(FileUtils.getFileMd5(file,multiPartFileInfo.getFileName() ,
                                off ,multiPartFileInfo.getLen() ));
                    multiPartFileInfo.setFilePath(fileDetailInfo.getFilePath());
                    part += 1;
                    multiPartFileInfo.setOff(off);

                    fileDetailInfo.addMultiPart(multiPartFileInfo);
                }
            }else{
                fileDetailInfo.setMultipart(false);
            }
        }else {
            throw new RuntimeException("操作异常！非文件或文件不存在");
        }
        return fileDetailInfo;
    }
    private String getFilePath(String fileId) {
        String filePath = "";
        if ("1".equals(fileId)) {
            filePath = "D:\\temp-E\\mac_file\\Thunder\\Julia-PPBD-148\\PPBD-148A.mp4";
        } else if ("2".equals(fileId)) {
            filePath = "D:\\Users\\Lucifer-Wi\\Desktop\\thymeleaf\\usingthymeleaf.pdf";
        }

        return filePath;
    }

    /**
     * 附件下载
     */
    @RequestMapping("/downloadFile")
    public void downloadFile(@RequestBody MultiPartFileInfo multiPartFileInfo , HttpServletResponse response , HttpServletRequest request) {
        //FIXME 优化代码
        File file = new File(multiPartFileInfo.getFilePath());
        if (file.exists() && file.isFile()) {
            OutputStream out = null;
            RandomAccessFile in = null;
            //下载起始位置
            long off = 0;
            int downloadSize = 0;
            try {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"",
                        new String(multiPartFileInfo.getFileName().getBytes("UTF-8"), "ISO8859-1")));

                response.setHeader("Accept-Ranges", "bytes");
                if (request.getHeader("Range") == null) {
                    response.setHeader("Content-Length", String.valueOf(multiPartFileInfo.getLen()));
                }else {
                    //解析断点续传
                    String range = request.getHeader("Range");
                    String[] bytes = range.replaceAll("bytes", "").split("-");
                    off = Long.parseLong(bytes[0]);
                    long end = 0;
                    if (bytes.length == 2) {
                        end = Long.parseLong(bytes[1]);
                    }
                    int length = 0;
                    if (end != 0 && end>off) {
                        length = Math.toIntExact(end - off);
                    }else{
                        length = Math.toIntExact(multiPartFileInfo.getLen() - off);
                    }
                    response.setHeader("Content-Length", String.valueOf(length));
                    downloadSize = length;
                }

                in = new RandomAccessFile(file,"rw");
                 out = response.getOutputStream();
                if (off == 0) {
                    off = multiPartFileInfo.getOff();
                }
                if (downloadSize == 0) {
                    downloadSize = Math.toIntExact(multiPartFileInfo.getLen());
                }
                byte[] bytes = new byte[fileOperationProperties.getReadBufLenSize()];
                int length = 0;
                //设置下载起始位置
                if (multiPartFileInfo.getOff() > 0) {
                    in.seek(off);
                }

                //预防读取超出分块范围大小
                long readContentLen = 0;
                if ((readContentLen + fileOperationProperties.getReadBufLenSize()) > downloadSize) {
                    bytes = new byte[Math.toIntExact(multiPartFileInfo.getLen() - readContentLen)];
                }
                while ((length = in.read(bytes)) !=-1 ) {
                    out.write(bytes,0,length);
                    readContentLen += length;
                }
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
