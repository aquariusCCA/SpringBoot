package com.test;

import com.test.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTest {
    @Autowired
    private MailService mailService;

    @Test
    void test(){
        mailService.sendSimpleText(
                "aquarius.c.c.a@gmail.com",         // from: 建議與 spring.mail.username 相同
                "aquarius.c.c.a@gmail.com",         // 收件者
                "測試郵件（純文字）",
                "這是一封由 Spring Boot 發出的測試郵件。"
        );
        System.out.println("Mail sent.");
    }


    @Test
    public void testHTMLMail() {
        String htmlContent = """
                <h1>這是一封由 Spring Boot 發出的測試郵件</h1>
                <p style="color:blue">這是一封 <b>HTML</b> 郵件。</p>
                <a href="https://www.google.com">Google</a>
                """;

        mailService.sendHTMLMail(
                "aquarius.c.c.a@gmail.com",         // from: 建議與 spring.mail.username 相同
                "aquarius.c.c.a@gmail.com",         // 收件者
                "測試郵件（純文字）",
                htmlContent
        );

        System.out.println("Mail sent.");
    }

    @Test
    public void testAttachmentMail() {
        String content = "這是一封帶有附件的郵件。請查收附件！";

        mailService.sendAttachmentMail(
                "aquarius.c.c.a@gmail.com",         // from: 建議與 spring.mail.username 相同
                "aquarius.c.c.a@gmail.com",         // 收件者
                "測試郵件（純文字）",
                "這是一封由 Spring Boot 發出的測試郵件。"
        );

        System.out.println("Mail sent.");
    }
}
