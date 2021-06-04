package com.yqj.qq.service;

import java.util.HashMap;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ClientTreadPool
 * Author: yaoqijun
 * Date: 2021/6/4 10:55
 */
public class ClientThreadPool {
    //线程池，保存当前登录成功的线程
    private static HashMap<String, ClientThread> hashMap = new HashMap<>();

    /**
     * 向线程池中添加用户线程
     *
     * @param userId       用户名
     * @param clientThread 线程对象
     */
    public static void addClient(String userId, ClientThread clientThread) {
        hashMap.put(userId, clientThread);
    }

    /**
     * 根据用户名获取该用户的线程对象
     *
     * @param userId 用户名
     */
    public static ClientThread getClient(String userId) {
        return hashMap.get(userId);
    }
}
