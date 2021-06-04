package com.yqj.qq.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ServerThreadPool
 * Author: yaoqijun
 * Date: 2021/6/4 14:36
 */
public class ServerThreadPool {
    //保持和客户端连接的服务端的socket线程
    private static HashMap<String, ServerThread> hashMap = new HashMap<>();

    public static HashMap<String, ServerThread> getHashMap() {
        return hashMap;
    }

    /**
     * 向线程池中添加线程
     *
     * @param userId       用户名
     * @param serverThread 对应的socket线程
     */
    public static void addServer(String userId, ServerThread serverThread) {
        hashMap.put(userId, serverThread);
    }

    /**
     * 从线程池获取对应的socket线程
     *
     * @param userId 用户名
     * @return 对应的线程
     */
    public static ServerThread getServer(String userId) {
        return hashMap.get(userId);
    }

    /**
     * 从线程池中删除对应的socket线程
     * @param userId
     */
    public static void removeServer(String userId){
        hashMap.remove(userId);
    }

    /**
     * 获取在线用户列表
     * @return 在线用户列表（各用户使用空格分隔）
     */
    public static String getOnlineList(){
        StringBuffer sb = new StringBuffer();
        //遍历集合
        Iterator<String> iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()){
            //构建在线用户列表
            sb.append(iterator.next()).append(" ");
        }
        return sb.toString();
    }
}
