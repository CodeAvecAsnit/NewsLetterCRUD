package com.news.lettercrud;

import com.news.lettercrud.Services.infrastructure.NotificationService;
import com.news.lettercrud.Services.infrastructure.impl.MailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

public class MailServiceTest {

        private final Logger logger = LoggerFactory.getLogger(com.news.lettercrud.Services.infrastructure.impl.MailServiceImpl.class);

        private final JavaMailSender javaMailSender;

        @Autowired
        public MailServiceTest(JavaMailSender javaMailSender) {
            this.javaMailSender = javaMailSender;
        }


        @Async
        public void  sendMail(String email,int code){
            SimpleMailMessage message = new SimpleMailMessage();
            try{
                message.setFrom("furnituremandu@gmail.com");
                message.setTo(email);
                message.setSubject("Verification Code for News CRUD");
                String body = "The verification code is "+code+". Please do not share this code.";
                message.setText(body);
                javaMailSender.send(message);
            }catch (Exception ex){
                logger.error("Some Error occurred while sending the email "+ ex.getMessage());
            }
        }
    }

}
