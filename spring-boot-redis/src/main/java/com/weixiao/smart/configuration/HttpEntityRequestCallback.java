package com.weixiao.smart.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/5/2 20:33
 */
public class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {

    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private final HttpEntity<?> requestEntity;

    public HttpEntityRequestCallback(@Nullable Object requestBody ) {
        this(requestBody, null);
    }

    public HttpEntityRequestCallback(@Nullable Object requestBody, @Nullable Type responseType) {
        super(responseType);
        if (requestBody instanceof HttpEntity) {
            this.requestEntity = (HttpEntity<?>) requestBody;
        }
        else if (requestBody != null) {
            this.requestEntity = new HttpEntity<>(requestBody);
        }
        else {
            this.requestEntity = HttpEntity.EMPTY;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
        super.doWithRequest(httpRequest);
        Object requestBody = this.requestEntity.getBody();
        if (requestBody == null) {
            HttpHeaders httpHeaders = httpRequest.getHeaders();
            HttpHeaders requestHeaders = this.requestEntity.getHeaders();
            if (!requestHeaders.isEmpty()) {
                requestHeaders.forEach((key, values) -> httpHeaders.put(key, new LinkedList<>(values)));
            }
            if (httpHeaders.getContentLength() < 0) {
                httpHeaders.setContentLength(0L);
            }
        }
        else {
            Class<?> requestBodyClass = requestBody.getClass();
            Type requestBodyType = (this.requestEntity instanceof RequestEntity ?
                    ((RequestEntity<?>)this.requestEntity).getType() : requestBodyClass);
            HttpHeaders httpHeaders = httpRequest.getHeaders();
            HttpHeaders requestHeaders = this.requestEntity.getHeaders();
            MediaType requestContentType = requestHeaders.getContentType();
            for (HttpMessageConverter<?> messageConverter : getRestTemplate().getMessageConverters()) {
                if (messageConverter instanceof GenericHttpMessageConverter) {
                    GenericHttpMessageConverter<Object> genericConverter =
                            (GenericHttpMessageConverter<Object>) messageConverter;
                    if (genericConverter.canWrite(requestBodyType, requestBodyClass, requestContentType)) {
                        if (!requestHeaders.isEmpty()) {
                            requestHeaders.forEach((key, values) -> httpHeaders.put(key, new LinkedList<>(values)));
                        }
                        if (logger.isDebugEnabled()) {
                            if (requestContentType != null) {
                                logger.debug("Writing [" + requestBody + "] as \"" + requestContentType +
                                        "\" using [" + messageConverter + "]");
                            }
                            else {
                                logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
                            }

                        }
                        genericConverter.write(requestBody, requestBodyType, requestContentType, httpRequest);
                        return;
                    }
                }
                else if (messageConverter.canWrite(requestBodyClass, requestContentType)) {
                    if (!requestHeaders.isEmpty()) {
                        requestHeaders.forEach((key, values) -> httpHeaders.put(key, new LinkedList<>(values)));
                    }
                    if (logger.isDebugEnabled()) {
                        if (requestContentType != null) {
                            logger.debug("Writing [" + requestBody + "] as \"" + requestContentType +
                                    "\" using [" + messageConverter + "]");
                        }
                        else {
                            logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
                        }

                    }
                    ((HttpMessageConverter<Object>) messageConverter).write(
                            requestBody, requestContentType, httpRequest);
                    return;
                }
            }
            String message = "Could not write request: no suitable HttpMessageConverter found for request type [" +
                    requestBodyClass.getName() + "]";
            if (requestContentType != null) {
                message += " and content type [" + requestContentType + "]";
            }
            throw new RestClientException(message);
        }
    }


}