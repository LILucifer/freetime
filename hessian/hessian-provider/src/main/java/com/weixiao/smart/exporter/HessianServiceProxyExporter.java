package com.weixiao.smart.exporter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author lishixiang0925@126.com.
 * @description (hessianHandlerProxy)
 * @Created 2018-12-31 15:38.
 */
@Slf4j
public class HessianServiceProxyExporter extends HessianServiceExporter {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("HessianServiceProxyExporter get request at {}" , LocalDateTime.now());
        super.handleRequest(request ,response);
    }


}
