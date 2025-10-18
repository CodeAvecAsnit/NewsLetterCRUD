package com.news.lettercrud.Services.infrastructure;

import org.springframework.scheduling.annotation.Async;

public interface NotificationService {
    @Async
    void  sendMail(String email, int code);
}
