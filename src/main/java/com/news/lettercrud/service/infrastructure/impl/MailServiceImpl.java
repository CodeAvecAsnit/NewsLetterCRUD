package com.news.lettercrud.service.infrastructure.impl;

import com.news.lettercrud.service.infrastructure.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */
@Service
@EnableAsync
public class MailServiceImpl implements NotificationService {

    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * @param email used to send mail verification message to the user
     */
    @Override
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
