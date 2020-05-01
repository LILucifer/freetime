package com.weixiao.smart.controller;

import com.weixiao.smart.model.response.Result;
import com.weixiao.smart.redis.jedis.RedisUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/4/11 21:53
 */
@RestController
@RequestMapping("/fileUpload")
public class FileUploadController {

    @RequestMapping("/upload")
    public Result upload(MultipartFile file , String fileMd5) {
        Result result = new Result();
        int verifySizeResult = verifySize(fileMd5, file.getSize());
        //上传起始位置
        int off = 0;
        //1、判断附件是否已经上传过 --- 24小时内
        if ( verifySizeResult > 0) {
            // 还未上传过
            result =fastDfsUpload(file, off, fileMd5);
        } else if (verifySizeResult < 0) {
            // 部分已上传
            off = Integer.valueOf(RedisUtils.get(fileMd5))-1;
            result =fastDfsUpload(file, off, fileMd5);
        }else{
            //已完成上传，直接返回成功结果
            result.setSuccess(true);
        }
        return result;
    }


    private Result fastDfsUpload(MultipartFile file, int off , String fileMd5) {
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
     *
     * @param fileMd5
     * @param length
     * @return 0 已完成上传 ， < 0 完成部分上传 ， > 0 未开始上传
     */
    int verifySize(String fileMd5 , Long length) {
        String cacheValue = RedisUtils.get(fileMd5);
        if (cacheValue != null) {
            return Long.valueOf(cacheValue).compareTo(length);
        }
        return 1;

    }
}
