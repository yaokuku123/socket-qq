package com.yqj.qq.service;

import com.yqj.qq.domain.Message;
import com.yqj.qq.domain.MessageType;
import com.yqj.qq.domain.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ServerService
 * Author: yaoqijun
 * Date: 2021/6/4 14:24
 */
public class ServerService {
    private ServerSocket serverSocket;

    private static HashMap<String, User> userPool = new HashMap<>(); //类似数据库，存储系统默认可以登录的用户

    //静态代码块初始化用户数据
    static {
        userPool.put("yorick", new User("yorick", "199748"));
        userPool.put("tom", new User("tom", "199748"));
        userPool.put("jerry", new User("jerry", "199748"));
    }

    //构造方法中监听端口9999
    public ServerService() {
        try {
            System.out.println("服务端在9999端口监听！");
            //启动发送消息的线程
            new Thread(new SendMessageService()).start();
            serverSocket = new ServerSocket(9999);
            while (true) {
                //接受一个socket客户端连接
                Socket socket = serverSocket.accept();
                //获取客户端发送过来的用户信息
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();
                //构建输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message message = new Message();
                //用户验证通过
                if (checkUser(user.getUserId(), user.getPassword())) {
                    message.setMessageType(MessageType.MSG_LOGIN_SUCCESS);
                    oos.writeObject(message);
                    //创建线程，持有对应的socket保持通讯
                    ServerThread serverThread = new ServerThread(socket, user.getUserId());
                    //启动线程
                    serverThread.start();
                    //将线程添加到线程池，方便管理
                    ServerThreadPool.addServer(user.getUserId(), serverThread);
                } else {
                    System.out.println("验证失败");
                    //验证失败，发送回去验证失败的消息
                    message.setMessageType(MessageType.MSG_LOGIN_FAIL);
                    oos.writeObject(message);
                    //关闭socket
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断客户端传递的用户是否和服务端的数据库中的数据相匹配
     *
     * @param userId   用户名
     * @param password 密码
     * @return 是否匹配
     */
    private boolean checkUser(String userId, String password) {
        User user = userPool.get(userId);
        if (user == null) {
            return false;
        }
        if (!user.getPassword().equals(password)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        new ServerService();
    }
}
