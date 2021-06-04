package com.yqj.qq.service;

import com.yqj.qq.domain.Message;
import com.yqj.qq.domain.MessageType;
import com.yqj.qq.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Iterator;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: SendMessageService
 * Author: yaoqijun
 * Date: 2021/6/4 22:54
 */
public class SendMessageService implements Runnable {

    @Override
    public void run() {
        while (true){
            System.out.print("请输入向在线用户集体发送的消息：");
            String content = Utility.readString(100);
            //退出发送消息的线程
            if ("exit".equals(content)){
                break;
            }
            //构建消息
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(content);
            message.setMessageType(MessageType.MSG_COMM_TO_ALL);
            message.setTimeStamp(new Date().toString());
            //遍历在线用户集合
            Iterator<String> iterator = ServerThreadPool.getHashMap().keySet().iterator();
            while (iterator.hasNext()){
                String userId = iterator.next();
                try {
                    //发送消息
                    ObjectOutputStream oos = new ObjectOutputStream(ServerThreadPool.getServer(userId).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
