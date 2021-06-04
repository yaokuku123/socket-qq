package com.yqj.qq.service;

import com.yqj.qq.domain.Message;
import com.yqj.qq.domain.MessageType;
import com.yqj.qq.domain.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: UserClientService
 * Author: yaoqijun
 * Date: 2021/6/4 10:37
 */
public class UserClientService {


    /**
     * 检查输入的用户名和密码是否在服务器端存在且正确
     *
     * @param userId   用户名
     * @param password 密码
     * @return 检查结果
     */
    public boolean check(String userId, String password) {
        boolean flag = false;
        //构造User对象
        User user = new User(userId, password);
        try {
            //和服务器建立socket
            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //传递用户信息到服务器
            oos.writeObject(user);
            //获取服务器返回的结果信息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();

            if (message.getMessageType().equals(MessageType.MSG_LOGIN_SUCCESS)) { //登录成功
                //创建一个客户端线程，在后台与服务器建立连接通道
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start(); //启动
                //放入线程池
                ClientThreadPool.addClient(userId, clientThread);
                //改变标志，说明登录成功
                flag = true;
            } else { //登录失败
                socket.close(); //关闭socket
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 获取当前在线用户列表
     * @param userId 用户名，用于获取对应用户名的线程
     */
    public void getOnlineUser(String userId){
        //获取对应的用户线程
        ClientThread clientThread = ClientThreadPool.getClient(userId);
        try {
            //构建获取在线列表的消息
            Message message = new Message();
            message.setSender(userId);
            message.setMessageType(MessageType.MSG_GET_ONLINE_LIST);
            //获取socket，并发送消息
            ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前用户退出系统
     * @param userId 用户名
     */
    public void logout(String userId){
        //获取对应的用户线程
        ClientThread clientThread = ClientThreadPool.getClient(userId);
        try {
            //构建获取在线列表的消息
            Message message = new Message();
            message.setMessageType(MessageType.MSG_CLIENT_EXIT);
            message.setSender(userId);
            //获取socket，并发送消息
            ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(userId + "正常退出");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
