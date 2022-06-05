package com.fzipp.pay.common.email;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;

//@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendEmailTest {

    @Autowired
    private SendEmail sendEmail;

    @Test
    public void testHtml() throws MessagingException {
        String content = "<html>\n" +
                "<body>\n" +
                "    <h1>这是Html格式邮件!,不信你看邮件，我字体比一般字体还要大</h1>\n" +
                "</body>\n" +
                "</html>";
        String[] tos = {"121582384@qq.com"};
        String title = "Html邮件";
        ToEmail toEmail = new ToEmail(tos, title, content);
        ResultEmail resultEmail = sendEmail.htmlEmail(toEmail);
        System.out.println(resultEmail);
    }
}
