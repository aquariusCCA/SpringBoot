package com.test.utils;

import org.springframework.stereotype.Component;

@Component
public class CodeUtils {
    private final String padding = "000000";

    // 生成验证码（位数少于6位左边填充0，填充方法1）
    public String generateCode(String phone) {
        int hash = phone.hashCode();
        int encryption = 20228888;
        long result = hash ^ encryption;
        long nowTime = System.nanoTime();
        result = (result ^ nowTime) % 1000000;
        String code = result + "";
        // code = phone;
        // padding.substring(code.length())   code.length()
        //                                         6
        //             0                           5
        //             00                          4
        //             000                         3
        //             000                         3
        //             0000                        2
        //             00000                       1
        //             000000                      0
        code = padding.substring(code.length()) + code;
        // System.out.println(code);
        return code;
    }
}