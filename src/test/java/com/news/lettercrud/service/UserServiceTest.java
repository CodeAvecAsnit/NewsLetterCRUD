package com.news.lettercrud.service;

import com.news.lettercrud.Services.model.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@Test
public class UserServiceTest {
    @Mock
    private final UserService userService;

    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @SpringBootTest
    public boolean test
}
