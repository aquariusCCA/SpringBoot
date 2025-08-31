package com.test.service.impl;

import com.test.pojo.SMSCode;
import com.test.service.SMSCodeService;
import com.test.utils.CodeUtils;
import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMSCodeServiceImpl implements SMSCodeService {
    @Autowired
    private CodeUtils codeUtils;


//    @Override
//    public String sendCodeToSMS(String tele) {
//        return null;
//    }
//
//    @Override
//    public boolean checkCode(SMSCode smsCode) {
//        return false;
//    }
    @Autowired
    private CacheChannel cacheChannel;

    public String sendCodeToSMS(String tele) {
        String code = codeUtils.generateCode(tele);
        cacheChannel.set("sms",tele,code);
        return code;
    }

    public boolean checkCode(SMSCode smsCode) {
        String code = cacheChannel.get("sms",smsCode.getPhone()).asString();
        return smsCode.getCode().equals(code);
    }
}