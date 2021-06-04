package com.yqj.qq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: Message
 * Author: yaoqijun
 * Date: 2021/6/4 9:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sender; //发送者
    private String getter; //接收者
    private String content; //消息内容
    private String timeStamp; //时间戳
    private String messageType; //消息类型

    private byte[] bytes; //文件字节流
    private int length; //文件长度
    private String src; //文件源地址
    private String dest; //文件目的地址

}
