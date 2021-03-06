package com.weixiao.smart.ebidding.intercept;

import com.weixiao.smart.ebidding.encrypt.RSAEncrypt;
import com.weixiao.smart.ebidding.encrypt.properties.EncryptProperties;
import com.weixiao.smart.ebidding.entity.Result;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @author lishixiang0925@126.com.
 * @description (自定义HtppMessageConvert for encrypt response data of com.weixiao.smart.ebidding.entity.Result)
 * @Created 2019-03-02 22:44.
 */
@Slf4j
@Component
public class CustomResultHttpMessageConvert extends AbstractHttpMessageConverter<Result> {

    @Autowired
    private EncryptProperties encryptProperties;

    @Override
    protected boolean supports(Class<?> clazz) {
        return Result.class.isAssignableFrom(clazz); //只处理com.weixiao.smart.ebidding.entity.Result 类型  返回数据
    }

    public CustomResultHttpMessageConvert() {
        super(MediaType.ALL);
    }

    @Override
    protected Result readInternal(Class<? extends Result> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        //数据设备号验签，数据解密
        inputMessage.getBody();
        return null;
    }

    @Override
    protected void writeInternal(Result result, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        log.info("CustomResultHttpMessageConvert response data :  {} ", JSONObject.fromObject(result));
        String body = JSONObject.fromObject(result).toString();
        if (encryptProperties.isEncryptOff()) {
            body = RSAEncrypt.encryptedDataByPublicKey(body, encryptProperties.getPublicKey());
            log.info("CustomResultHttpMessageConvert response encrypt data  :  {}", body);
        }
        StreamUtils.copy(body, StandardCharsets.UTF_8, outputMessage.getBody());


    }


}
