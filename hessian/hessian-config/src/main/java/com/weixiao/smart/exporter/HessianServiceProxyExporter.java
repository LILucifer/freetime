package com.weixiao.smart.exporter;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.services.server.AbstractSkeleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author lishixiang0925@126.com.
 * @description (hessianHandlerProxy)
 * @Created 2018-12-31 15:38.
 */
@Slf4j
public class HessianServiceProxyExporter extends HessianServiceExporter {
    private SerializerFactory serializerFactory = new SerializerFactory();


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //此处可做统一数据验签

        if (!"POST".equals(request.getMethod())) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(),
                    new String[]{"POST"}, "HessianServiceExporter only supports POST requests");
        }

        response.setContentType(CONTENT_TYPE_HESSIAN);
        InputStream inputStreamTeam = null;
        try {
            InputStream inputStream = request.getInputStream();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Fake code simulating the copy
            // You can generally do better with nio if you need...
            // And please, unlike me, do something about the Exceptions :D
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            // Open new InputStreams using the recorded bytes
            // Can be repeated as many times as you wish
            InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
            InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

            printRequestData(is1);
            invoke(is2, response.getOutputStream());
        } catch (Throwable ex) {
            throw new NestedServletException("Hessian skeleton invocation failed", ex);
        } finally {
            if (inputStreamTeam != null) {
                inputStreamTeam.close();
            }
        }
    }

    /**
     * 输出hessian请求日志
     *
     * @param isToUse 请求输入流
     */
    private void printRequestData(InputStream isToUse) {
        AbstractHessianInput in = null;
        try {
            if (!isToUse.markSupported()) {
                isToUse = new BufferedInputStream(isToUse);
                isToUse.mark(1);
            }

            int code = isToUse.read();
            int major;
            int minor;

            if (code == 'H') {
                // Hessian 2.0 stream
                major = isToUse.read();
                minor = isToUse.read();
                if (major != 0x02) {
                    throw new IOException("Version " + major + "." + minor + " is not understood");
                }
                in = new Hessian2Input(isToUse);
                in.readCall();
            } else if (code == 'C') {
                // Hessian 2.0 call... for some reason not handled in HessianServlet!
                isToUse.reset();
                in = new Hessian2Input(isToUse);
                in.readCall();
            } else if (code == 'c') {
                // Hessian 1.0 call
                major = isToUse.read();
                minor = isToUse.read();
                in = new HessianInput(isToUse);
            } else {
                throw new IOException("Expected 'H'/'C' (Hessian 2.0) or 'c' (Hessian 1.0) in hessian input at " + code);
            }
            in.setSerializerFactory(this.serializerFactory);

            // backward compatibility for some frameworks that don't read
            // the call type first
            in.skipOptionalCall();

            Class<?> apiClass = getServiceInterface();
            String methodName = in.readMethod();
            int argLength = in.readMethodArgLength();
            Method method = getMethod(apiClass, methodName, argLength);
            if (method == null) {
            }
            Class<?>[] args = method.getParameterTypes();
            if (argLength != args.length && argLength >= 0) {
                log.error("NoSuchMethod method {} argument length mismatch, received length= {}", method, argLength);
                return;
            }
            Object[] values = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                // XXX: needs Marshal object
                values[i] = in.readObject(args[i]);
            }
            log.info("hessian request  Method = {}.{} ,  Arguments = {}", apiClass, methodName, values);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (isToUse != null) {
                    isToUse.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * get hessian  request target Method
     *
     * @param methodName 方法名
     * @param argLength  参数长度
     * @return
     */
    private Method getMethod(Class<?> apiClass, String methodName, int argLength) {
        HashMap _methodMap = new HashMap();
        Method[] methodList = apiClass.getMethods();
        for (int i = 0; i < methodList.length; i++) {
            Method method = methodList[i];

            if (_methodMap.get(method.getName()) == null)
                _methodMap.put(method.getName(), methodList[i]);

            Class[] param = method.getParameterTypes();
            String mangledName = method.getName() + "__" + param.length;
            _methodMap.put(mangledName, methodList[i]);

            _methodMap.put(AbstractSkeleton.mangleName(method, false), methodList[i]);
        }
        Method method;
        method = (Method) _methodMap.get(methodName + "__" + argLength);
        if (method == null) {
            method = (Method) _methodMap.get(methodName);
        }
        return method;
    }


}
