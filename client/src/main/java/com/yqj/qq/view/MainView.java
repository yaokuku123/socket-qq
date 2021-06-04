package com.yqj.qq.view;

import com.yqj.qq.service.FileService;
import com.yqj.qq.service.MessageService;
import com.yqj.qq.service.UserClientService;
import com.yqj.qq.utils.Utility;

import java.util.Scanner;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: MainView
 * Author: yaoqijun
 * Date: 2021/6/4 10:17
 */
public class MainView {

    private boolean loop = true; //控制循环

    private String key = ""; //读取输入

    private UserClientService userClientService = new UserClientService();

    private MessageService messageService = new MessageService();

    private FileService fileService = new FileService();

    public static void main(String[] args) {
        new MainView().mainView();
    }

    private void mainView() {
        while (loop) {
            System.out.println("==========欢迎登录网络通讯系统==========");
            System.out.println("\t\t1 登录系统");
            System.out.println("\t\t9 退出系统");
            System.out.print("请输入你的选择：");
            key = Utility.readString(1);
            switch (key) {
                case "1":
                    System.out.print("请输入用户名：");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密 码：");
                    String password = Utility.readString(50);
                    if (userClientService.check(userId, password)) {
                        while (loop) {
                            System.out.println("==========网络通讯系统二级菜单，用户名：" + userId);
                            System.out.println("\t\t1 显示在线用户列表");
                            System.out.println("\t\t2 群发消息");
                            System.out.println("\t\t3 私聊消息");
                            System.out.println("\t\t4 发送文件");
                            System.out.println("\t\t9 退出系统");
                            System.out.print("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    userClientService.getOnlineUser(userId);
                                    break;
                                case "2":
                                    System.out.print("请输入群发消息内容：");
                                    String contentToAll = Utility.readString(100);
                                    messageService.sendMsgToAll(userId,contentToAll);
                                    break;
                                case "3":
                                    System.out.print("请输入要发送的人：");
                                    String getter = Utility.readString(50);
                                    System.out.print("请输入要发送的消息：");
                                    String content = Utility.readString(100);
                                    messageService.sendMsg(userId,getter,content);
                                    break;
                                case "4":
                                    System.out.print("请输入文件要发送的人：");
                                    getter = Utility.readString(50);
                                    System.out.print("请输入源文件路径：");
                                    String src = Utility.readString(100);
                                    System.out.print("请输入目的文件路径：");
                                    String dest = Utility.readString(100);
                                    fileService.uploadFile(userId,getter,src,dest);
                                    break;
                                case "9":
                                    userClientService.logout(userId);
                                    loop = false;
                                    System.out.println("您成功退出系统，欢迎下次使用！");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("用户名或密码错误，登录失败！");
                    }
                    break;
                case "9":
                    loop = false;
                    System.out.println("您成功退出系统，欢迎下次使用！");
                    break;
            }
        }
    }
}
