package com.weixiao.smart.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-09 22:21.
 */
@Slf4j
public class SocketService {
    public static void service() {
        BufferedWriter out = null;
        BufferedReader bin = null;
        Socket client = null;
        ServerSocket serverSocket = null;
        try {
            log.info("SocketServer start!");
            int port = 9999;
            serverSocket = new ServerSocket(port);

            //向Client 返回结果
            String message;
            while (true) {
                client = serverSocket.accept();
                log.info("SocketServer receive start!");
                bin = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                while ((message = bin.readLine()) != null) {
                    log.info("socket server receive data = {}", message);
                    out.write("server back : " + System.currentTimeMillis());
                    out.newLine();
                    out.flush();

                }
                log.info("SocketServer done");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //out.close();
                bin.close();
                client.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        service();
    }
}
