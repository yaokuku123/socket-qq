package com.yqj.qq.service;

import com.yqj.qq.domain.Message;
import com.yqj.qq.domain.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ServerThread
 * Author: yaoqijun
 * Date: 2021/6/4 14:29
 */
public class ServerThread extends Thread {
    private Socket socket;
    private String userId;

    public ServerThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        System.out.println("服务端线程，等待获取从" + userId + "客户端发送的消息");
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if (message.getMessageType().equals(MessageType.MSG_GET_ONLINE_LIST)) {
                    //获取在线用户列表
                    System.out.println(message.getSender() + "获取在线用户列表！");
                    //获取在线用户列表
                    String onlineList = ServerThreadPool.getOnlineList();
                    //构建发送消息
                    Message retMessage = new Message();
                    retMessage.setMessageType(MessageType.MSG_RET_ONLINE_LIST);
                    retMessage.setContent(onlineList);
                    retMessage.setGetter(message.getSender());
                    //发送消息
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(retMessage);
                } else if (message.getMessageType().equals(MessageType.MSG_CLIENT_EXIT)) {
                    //客户端退出
                    System.out.println(message.getSender() + "与服务器连接正常中断");
                    ServerThreadPool.removeServer(message.getSender());
                    socket.close();
                    break;
                } else if (message.getMessageType().equals(MessageType.MSG_COMM)) {
                    //私信消息转发
                    System.out.println("接收到从" + message.getSender() + "向" + message.getGetter() + "发送的消息");
                    //转发私信消息
                    ServerThread serverThread = ServerThreadPool.getServer(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } else if (message.getMessageType().equals(MessageType.MSG_COMM_TO_ALL)) {
                    //群发消息转发
                    System.out.println("接收到从" + message.getSender() + "向大家发送的消息");
                    //遍历获取在线用户
                    Iterator<String> iterator = ServerThreadPool.getHashMap().keySet().iterator();
                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        //排除发送消息者
                        if (!next.equals(message.getSender())) {
                            ServerThread serverThread = ServerThreadPool.getServer(next);
                            ObjectOutputStream oos = new ObjectOutputStream(serverThread.getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                } else if (message.getMessageType().equals(MessageType.MSG_FILE)) {
                    //上传文件
                    System.out.println("接收到" + message.getSender() + "向" + message.getGetter() + "发送的文件");
                    //转发文件
                    ServerThread serverThread = ServerThreadPool.getServer(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } else {
                    System.out.println("其他操作");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
