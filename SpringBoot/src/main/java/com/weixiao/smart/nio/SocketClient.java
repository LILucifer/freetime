package com.weixiao.smart.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-11 11:16.
 */
@Slf4j
public class SocketClient {
    public static void postMsg() {
        BufferedReader input = null;
        BufferedReader inputStreamResult = null;
        BufferedWriter out = null;
        Socket socket = null;
        try {
            log.info("sds");
            //获取键盘输入
            input = new BufferedReader(new InputStreamReader(System.in));
            socket = new Socket("127.0.0.1", 9999);
            //获取Socket的输出流，用来发送数据到服务端
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //获取ServerSocket 返回的结果
            inputStreamResult = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int count = 0;
            String message = "";
            while (((message = input.readLine())!=null)&&count < 2) {
                out.write(message);
                out.newLine();
                out.flush();
                String result = inputStreamResult.readLine();
                log.info("ServerSocket result = {}", result);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                input.close();
                //inputStreamResult.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        postMsg();
    }
}
