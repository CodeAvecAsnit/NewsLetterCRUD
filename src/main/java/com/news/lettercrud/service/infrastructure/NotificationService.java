package com.news.lettercrud.service.infrastructure;

import org.springframework.scheduling.annotation.Async;

public interface NotificationService {
    @Async
    void  sendMail(String email, int code);
}
