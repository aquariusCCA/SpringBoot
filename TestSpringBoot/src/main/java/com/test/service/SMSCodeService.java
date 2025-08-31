package com.test.service;

import com.test.pojo.SMSCode;

public interface SMSCodeService {
    String sendCodeToSMS(String phone);

    boolean checkCode(SMSCode smsCode);
}