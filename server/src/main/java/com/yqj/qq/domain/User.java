package com.yqj.qq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: User
 * Author: yaoqijun
 * Date: 2021/6/4 9:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId; //用户名
    private String password; //密码
}
