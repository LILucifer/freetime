package com.weixiao.smart.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/5/2 20:41
 */
public class AcceptHeaderRequestCallback implements RequestCallback {
    private RestTemplate restTemplate;
    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    @Nullable
    private final Type responseType;

    public AcceptHeaderRequestCallback(@Nullable Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public void doWithRequest(ClientHttpRequest request) throws IOException {
        if (this.responseType != null) {
            Class<?> responseClass = null;
            if (this.responseType instanceof Class) {
                responseClass = (Class<?>) this.responseType;
            }
            List<MediaType> allSupportedMediaTypes = new ArrayList<>();
            for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
                if (responseClass != null) {
                    if (converter.canRead(responseClass, null)) {
                        allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                    }
                }
                else if (converter instanceof GenericHttpMessageConverter) {
                    GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
                    if (genericConverter.canRead(this.responseType, null, null)) {
                        allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                    }
                }
            }
            if (!allSupportedMediaTypes.isEmpty()) {
                MediaType.sortBySpecificity(allSupportedMediaTypes);
                if (logger.isDebugEnabled()) {
                    logger.debug("Setting request Accept header to " + allSupportedMediaTypes);
                }
                request.getHeaders().setAccept(allSupportedMediaTypes);
            }
        }
    }

    private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
        List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
        List<MediaType> result = new ArrayList<>(supportedMediaTypes.size());
        for (MediaType supportedMediaType : supportedMediaTypes) {
            if (supportedMediaType.getCharset() != null) {
                supportedMediaType =
                        new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
            }
            result.add(supportedMediaType);
        }
        return result;
    }
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
