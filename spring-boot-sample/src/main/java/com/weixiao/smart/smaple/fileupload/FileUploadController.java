package com.weixiao.smart.smaple.fileupload;

import com.weixiao.smart.smaple.entity.Result;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description 附件上傳下載
 * @Created 2020-04-22 21:07.
 */
@RestController
@RequestMapping("/fileUpload")
public class FileUploadController {

    @RequestMapping("/upload")
    public Result uploadFile() {
        Result result = new Result();
        //1.判断
        return result;
    }
}
