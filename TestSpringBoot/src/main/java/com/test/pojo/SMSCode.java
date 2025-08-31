package com.test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
// 存储手机号和验证码的实体类
public class SMSCode {
    private String phone;
    private String code;
}