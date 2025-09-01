package com.test.service.impl;

import com.test.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 發送最簡單的純文字郵件
     */
    public void sendSimpleText(String from, String to, String subject, String content) {
        SimpleMailMessage msg = new SimpleMailMessage();
        // 多數供應商要求 from 必須等於/授權於 username
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(content);
        javaMailSender.send(msg);
    }

    @Override
    public void sendHTMLMail(String from, String to, String subject, String content) {
                try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);		//此处设置正文支持html解析

            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void sendMail() {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message,true);		//此处设置支持附件
//            helper.setFrom(to+"(小甜甜)");
//            helper.setTo(from);
//            helper.setSubject(subject);
//            helper.setText(context);
//
//            //添加附件
//            File f1 = new File("springboot_23_mail-0.0.1-SNAPSHOT.jar");
//            File f2 = new File("resources\\logo.png");
//
//            helper.addAttachment(f1.getName(),f1);
//            helper.addAttachment("最靠谱的培训结构.png",f2);
//
//            javaMailSender.send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    public void sendAttachmentMail(String from, String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);		//此处设置支持附件
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);

            //添加附件
            // 從 classpath 讀取（可執行 JAR 也適用）
            ClassPathResource res = new ClassPathResource("test.jpg");
            helper.addAttachment("最靠谱的培训结构.png", res);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}