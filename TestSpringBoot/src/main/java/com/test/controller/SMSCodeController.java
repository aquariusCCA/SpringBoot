package com.test.controller;

import com.test.pojo.SMSCode;
import com.test.service.SMSCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/smscode")
public class SMSCodeController {
    @Autowired
    SMSCodeService smsCodeService;

    @GetMapping("{phone}")
    public String getCode(@PathVariable String phone) {
        return smsCodeService.sendCodeToSMS(phone);
    }

    @PostMapping
    public boolean checkCode(@RequestBody SMSCode smsCode) {
        return smsCodeService.checkCode(smsCode);
    }
}
