package com.yqj.qq.domain;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: MessageType
 * Author: yaoqijun
 * Date: 2021/6/4 10:05
 */
public interface MessageType {

    String MSG_LOGIN_SUCCESS = "1"; //登录成功
    String MSG_LOGIN_FAIL = "2"; //登录失败
    String MSG_COMM = "3"; //普通消息
    String MSG_GET_ONLINE_LIST = "4"; //获取在线列表
    String MSG_RET_ONLINE_LIST = "5"; //返回在线列表
    String MSG_CLIENT_EXIT = "6"; //客户端退出
    String MSG_COMM_TO_ALL = "7"; //群发消息
    String MSG_FILE = "8"; //上传的文件消息
}
