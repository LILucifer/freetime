import com.alibaba.fastjson.JSONObject;
import com.weixiao.smart.RedisDemoApplication;
import com.weixiao.smart.configuration.HttpEntityRequestCallback;
import com.weixiao.smart.configuration.properties.FileOperationProperties;
import com.weixiao.smart.file.FileJob;
import com.weixiao.smart.file.FileUtils;
import com.weixiao.smart.file.models.FileDetailInfo;
import com.weixiao.smart.file.models.FileUploadResultModel;
import com.weixiao.smart.file.models.MultiPartFileInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author lishixiang
 * @Title:
 * @Description: 附件操作测试
 * @date 2020/5/1 21:54
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RedisDemoApplication.class})
@AutoConfigureMockMvc
public class FileOperationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FileOperationProperties fileOperationProperties;

    /**
     * 在每次测试执行前构建mvc环境
     */
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testFileUploadNotPart() {

        try {
            //Step1 查询分片信息
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/fileOperation/getFileDetailInfo").param("fileId", "1"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            String body = mvcResult.getResponse().getContentAsString();
            FileDetailInfo fileDetailInfo = new JSONObject().parseObject(body, FileDetailInfo.class);

            //创建临时存储文件夹
            File directFile = new File(fileOperationProperties.getTempDirect());
            if (!directFile.exists()) {
                directFile.mkdirs();
            }

            if (FileUtils.enoughFreeSpace(fileOperationProperties.getTempDirect(), (long) (fileDetailInfo.getSize() * 1.3))) {
                //Step2 下载附件
                if (fileDetailInfo.isMultipart()) {
                    //Step2.1 分片下载
                    int nThreads = fileOperationProperties.getNThreads();
                    int threadPoolQueueCapacity = fileOperationProperties.getThreadPoolQueueCapacity();
                    ExecutorService executorService = new ThreadPoolExecutor(nThreads, nThreads, 0L,
                            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(threadPoolQueueCapacity));
                    //Step2.1.1 线程池中下载
                    //FIXME 优化：1、获取的分片信息是个有序集合，按照一定的顺序下载。-->及时处理已下载完成的分片，尽可能合理利用存储空间
                    //FIXME 优化：2、下载完成一个分片 合并处理一个分片  不适用阻塞
                    Queue<Future<FileUploadResultModel>> futureQueue = new ConcurrentLinkedQueue<Future<FileUploadResultModel>>();
                    for (MultiPartFileInfo multiPartFileInfo : fileDetailInfo.getMultiPartFileInfos()) {
                        Future<FileUploadResultModel> futreTask = executorService.submit(new FileJob(restTemplate,
                                multiPartFileInfo, fileOperationProperties));
                        futureQueue.add(futreTask);
                    }

                    int sizeOfSuccessPartFile = 0;
                    for (Future<FileUploadResultModel> resultModelFuture : futureQueue) {
                        FileUploadResultModel resultModel = resultModelFuture.get();
                        if (resultModel.isSuccess()) {
                            sizeOfSuccessPartFile += 1;
                        } else {
                            //下载失败处理--记录下载失败原因/重试下载
                        }
                    }

                    if (sizeOfSuccessPartFile == fileDetailInfo.getMultiPartFileInfos().size()) {
                        //Step2.1.2 下载成功 合并附件
                        String filePath = fileOperationProperties.getTempDirect() + File.separator + fileDetailInfo.getFileName();
                        //预创建与源文件相同大小的文件
                        File file = new File(filePath);
                        if (file.exists() && file.isFile()) {
                            file.delete();
                            file.createNewFile();
                        } else {
                            file.createNewFile();
                        }
                        //FIXME 此处使用多线程合并文件，提高合并处理效率
                        RandomAccessFile rFile = new RandomAccessFile(file, "rw");
                        rFile.setLength(fileDetailInfo.getSize());
                        for (Future<FileUploadResultModel> resultModelFuture : futureQueue) {
                            FileUploadResultModel fileUploadResultModel = resultModelFuture.get();
                            MultiPartFileInfo multiPartFileInfo = fileUploadResultModel.getMultiPartFileInfo();
                            //设置写入起始位置
                            rFile.seek(multiPartFileInfo.getOff());
                            byte[] bytes = new byte[fileOperationProperties.getReadBufLenSize()];
                            int length = 0;
                            File tempFile = new File(fileUploadResultModel.getLocalFilePath());
                            InputStream TempFileInputStream = new FileInputStream(tempFile);
                            while ((length = TempFileInputStream.read(bytes)) != -1) {
                                rFile.write(bytes, 0, length);
                            }
                            TempFileInputStream.close();
                            tempFile.delete();
                        }
                        //Step2.1.3 校验附件
                        if (FileUtils.checkFile(filePath, fileDetailInfo.getFileMd5(), fileDetailInfo.getSize())) {
                            log.info("附件下载成功！附件本地目录 {}", filePath);
                        }

                    }
                } else {
                    //step2.2 整个附件下载
                    MultiPartFileInfo multiPartFileInfo = new MultiPartFileInfo();
                    multiPartFileInfo.setFilePath(fileDetailInfo.getFilePath());
                    multiPartFileInfo.setOff(0);
                    multiPartFileInfo.setFileName(fileDetailInfo.getFileName());
                    multiPartFileInfo.setFileMd5(fileDetailInfo.getFileMd5());
                    multiPartFileInfo.setLen(fileDetailInfo.getSize());
                    //下载附件
                    FileJob fileJob = new FileJob(restTemplate,
                            multiPartFileInfo, fileOperationProperties);
                    FileUploadResultModel resultModel = fileJob.uploadFile();
                    //step3 校验附件MD5
                    if (FileUtils.checkFile(resultModel.getLocalFilePath(), fileDetailInfo.getFileMd5(), fileDetailInfo.getSize())) {
                        log.info("附件下载成功！附件本地目录 {}", resultModel.getLocalFilePath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFileMd5() {
        String filePath = "D:\\Users\\Lucifer-Wi\\Desktop\\thymeleaf\\usingthymeleaf.pdf";
        log.info("md5 = {}", FileUtils.getFileMd5(filePath));
        File file = new File(filePath);
        try {
            log.info("md5 = {}", FileUtils.getFileMd5(file, 2 + filePath, 0, file.length()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
