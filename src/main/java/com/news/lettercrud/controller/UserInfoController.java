package com.news.lettercrud.controller;

import com.news.lettercrud.service.news.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(@Qualifier("userInfoServiceImpl") UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getUsersPosts(@RequestParam(name = "id")long userId){
        return ResponseEntity.ok(userInfoService.getUsersNews(userId));
    }
}
