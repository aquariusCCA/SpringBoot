package com.test.service;

import jakarta.mail.MessagingException;

public interface MailService {
    // 发送简单消息
    void sendSimpleText(String from, String to, String subject, String content);

    void sendHTMLMail(String from, String to, String subject, String content);

    void sendAttachmentMail(String from, String to, String subject, String content);
}
