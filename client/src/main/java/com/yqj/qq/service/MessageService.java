package com.yqj.qq.service;

import com.yqj.qq.domain.Message;
import com.yqj.qq.domain.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: MessageService
 * Author: yaoqijun
 * Date: 2021/6/4 20:10
 */
public class MessageService {

    /**
     * 发送消息
     *
     * @param sender  发送者
     * @param getter  接收者
     * @param content 内容
     */
    public void sendMsg(String sender, String getter, String content) {
        System.out.println(sender + "向" + getter + "发送了消息：" + content);
        //构造消息体
        Message message = new Message();
        message.setSender(sender);
        message.setGetter(getter);
        message.setMessageType(MessageType.MSG_COMM);
        message.setContent(content);
        message.setTimeStamp(new Date().toString());
        //发送消息
        try {
            ClientThread clientThread = ClientThreadPool.getClient(sender);
            ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     *
     * @param sender  发送者
     * @param content 内容
     */
    public void sendMsgToAll(String sender, String content) {
        System.out.println(sender + "向大家发送了消息：" + content);
        //构造消息体
        Message message = new Message();
        message.setSender(sender);
        message.setMessageType(MessageType.MSG_COMM_TO_ALL);
        message.setContent(content);
        message.setTimeStamp(new Date().toString());
        //发送消息
        try {
            ClientThread clientThread = ClientThreadPool.getClient(sender);
            ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
