package com.weixiao.smart.ebidding.configure;

import com.weixiao.smart.ebidding.intercept.RequestHandlerInterceptorAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(加载自定义配置)
 * @date 2019/2/25 17:44
 */
@Configuration
public class WebConfigure  extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestHandlerInterceptorAdapter());
        super.addInterceptors(registry);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
    }
}
