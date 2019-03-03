package com.weixiao.smart.ebidding.intercept;

import com.weixiao.smart.ebidding.encrypt.RSAEncrypt;
import com.weixiao.smart.ebidding.encrypt.properties.EncryptProperties;
import com.weixiao.smart.ebidding.entity.Result;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/2/26 16:40
 */
@Slf4j
@ControllerAdvice(basePackages = "com.weixiao.smart.ebidding.controller")
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice {

    @Autowired
    private EncryptProperties encryptProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * Invoked after an {@code HttpMessageConverter} is selected and just before
     * its write method is invoked.
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     * @return the body that was passed in or a modified, possibly new instance
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //com.weixiao.smart.ebidding.entity.Result 类型外的返回数据都在ResponseBodyAdvice 加密处理后返回。Result类型数据有CustomResultHttpMessageConvert 处理
        log.info("ResponseBodyAdvice response data :  {} ", body);
        if (!Result.class.isAssignableFrom(body.getClass()) && encryptProperties.isEncryptOff()) {
            body = JSONObject.fromObject(body).toString();
            body = RSAEncrypt.encryptedDataByPublicKey((String) body, encryptProperties.getPublicKey());
            log.info("ResponseBodyAdvice response encrypt data :  {}", body);
        }
        return body;
    }
}
