package com.weixiao.smart.file;

import com.weixiao.smart.configuration.HttpEntityRequestCallback;
import com.weixiao.smart.configuration.properties.FileOperationProperties;
import com.weixiao.smart.file.models.FileUploadResultModel;
import com.weixiao.smart.file.models.MultiPartFileInfo;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * @author lishixiang
 * @Title:
 * @Description: 下载处理
 * @date 2020/5/5 16:01
 */
public class FileJob implements Callable<FileUploadResultModel> {
    private RestTemplate restTemplate ;
    private MultiPartFileInfo multiPartFileInfo;
    private FileOperationProperties fileOperationProperties;

    public FileJob(RestTemplate restTemplate, MultiPartFileInfo multiPartFileInfo, FileOperationProperties fileOperationProperties) {
        this.restTemplate = restTemplate;
        this.multiPartFileInfo = multiPartFileInfo;
        this.fileOperationProperties = fileOperationProperties;
    }

    @Override
    public FileUploadResultModel call() throws Exception {
        return uploadFile();
    }

    public FileUploadResultModel  uploadFile() {
        HttpEntityRequestCallback requestCallback =new HttpEntityRequestCallback(multiPartFileInfo);
        requestCallback.setRestTemplate(restTemplate);
        FileUploadResultModel result = restTemplate.execute(fileOperationProperties.getFileUploadUrlForJunitTest(),
                HttpMethod.POST, requestCallback, new ResponseExtractor<FileUploadResultModel>() {
                    @Nullable
                    @Override
                    public FileUploadResultModel extractData(ClientHttpResponse response) throws IOException {
                        FileUploadResultModel resultModel = new FileUploadResultModel();
                        //判断是否有足够的空间存放附件
                        if (FileUtils.enoughFreeSpace(fileOperationProperties.getTempDirect(), multiPartFileInfo.getLen())) {
                            String tempPath = fileOperationProperties.getTempDirect() + File.separator + multiPartFileInfo.getFileName();
                            File file = new File(tempPath);
                            if (file.exists() && file.isFile()) {
                                file.delete();
                                file.createNewFile();
                            }else{
                                file.createNewFile();
                            }
                            InputStream inputStream = response.getBody();
                            OutputStream outputStream = new FileOutputStream(file);
                       /* StreamUtils.copy(inputStream, outputStream);
                        Assertions.assertThat(file.length()).isLessThanOrEqualTo(multiPartFileInfo.getLen());*/
                            byte[] bytes = new byte[fileOperationProperties.getReadBufLenSize()];
                            int length = 0;
                            while ((length = inputStream.read(bytes)) != -1) {
                                outputStream.write(bytes,0,length);
                            }
                            outputStream.flush();
                            inputStream.close();
                            outputStream.close();

                            resultModel.setMessage("附件下载成功");
                            resultModel.setSuccess(true);
                            resultModel.setMultiPartFileInfo(multiPartFileInfo);
                            resultModel.setLocalFilePath(tempPath);
                        }else{
                            resultModel.setMessage("磁盘空间不足！");
                            resultModel.setSuccess(false);
                            resultModel.setMultiPartFileInfo(multiPartFileInfo);
                        }
                        return resultModel;
                    }
                },"");
        return result;

    }
}
