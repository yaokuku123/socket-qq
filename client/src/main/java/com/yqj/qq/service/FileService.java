package com.yqj.qq.service;

import com.yqj.qq.domain.Message;
import com.yqj.qq.domain.MessageType;

import java.io.*;
import java.util.Date;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FileService
 * Author: yaoqijun
 * Date: 2021/6/4 22:06
 */
public class FileService {

    /**
     * 上传文件
     *
     * @param sender 发送者
     * @param getter 接收者
     * @param src    源文件路径
     * @param dest   目的文件路径
     */
    public void uploadFile(String sender, String getter, String src, String dest) {
        System.out.println(sender + "将文件发送给" + getter + "，源文件路径：" + src + "，目的文件路径" + dest);
        //构建消息
        Message message = new Message();
        message.setSender(sender);
        message.setGetter(getter);
        message.setMessageType(MessageType.MSG_FILE);
        message.setSrc(src);
        message.setDest(dest);
        message.setTimeStamp(new Date().toString());

        //输入流读取文件到内存
        FileInputStream fis = null;
        byte[] bytes = new byte[(int) new File(src).length()];
        try {
            fis = new FileInputStream(src);
            fis.read(bytes);
            message.setBytes(bytes);
            message.setLength(bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //发送消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ClientThreadPool.getClient(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
