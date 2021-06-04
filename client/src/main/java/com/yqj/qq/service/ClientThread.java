package com.yqj.qq.service;

import com.yqj.qq.domain.Message;
import com.yqj.qq.domain.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ClientThread
 * Author: yaoqijun
 * Date: 2021/6/4 10:43
 */
public class ClientThread extends Thread {
    private Socket socket; //该线程连接的socket

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //在后台与服务器进行通讯，接受服务器返回的信息
        while (true) {
            System.out.println("\n客户端线程，等待获取从服务器端发送的消息");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getMessageType().equals(MessageType.MSG_RET_ONLINE_LIST)) {
                    //接收到显示在线用户列表的消息
                    String[] users = message.getContent().split(" ");
                    System.out.println("\n在线列表：");
                    for (String user : users) {
                        System.out.println("用户：" + user);
                    }
                } else if (message.getMessageType().equals(MessageType.MSG_COMM)) {
                    //接收到显示发送的私信消息
                    System.out.println("\n从" + message.getSender() + "向" + message.getGetter() + "发送的消息是：" +
                            message.getContent());
                } else if (message.getMessageType().equals(MessageType.MSG_COMM_TO_ALL)) {
                    //接收群发消息并显示
                    System.out.println("\n从" + message.getSender() + "向大家发送的消息是：" + message.getContent());
                } else if (message.getMessageType().equals(MessageType.MSG_FILE)) {
                    System.out.println("\n接收到" + message.getSender() + "向" + message.getGetter() + "发送的文件，源文件路径：" +
                            message.getSrc() + "，目的文件路径：" + message.getDest());
                    //上传的文件保存到本地
                    FileOutputStream fos = new FileOutputStream(message.getDest());
                    fos.write(message.getBytes());
                    fos.close();
                    System.out.println("上传成功");
                } else {
                    System.out.println("\n其他操作");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
