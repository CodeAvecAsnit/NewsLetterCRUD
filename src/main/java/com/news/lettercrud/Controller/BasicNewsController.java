package com.news.lettercrud.Controller;

import com.news.lettercrud.Security.UserDetailsImpl;
import com.news.lettercrud.Services.news.BasicNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class BasicNewsController {

    private final BasicNewsService basicNewsService;

    @Autowired
    public BasicNewsController(@Qualifier("basicNewsServiceImpl") BasicNewsService basicNewsService) {
        this.basicNewsService = basicNewsService;
    }

    @GetMapping("/today")
    public ResponseEntity<?> getNewsForToday(){
        return ResponseEntity.ok(basicNewsService.getTodayNews());
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteNewsByUser(@RequestParam int newsId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        basicNewsService.deleteNewsByUser(userDetails.getId(),newsId);
        return ResponseEntity.ok("Success");
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> deleteNewsByAdmin(@RequestParam int newsId){
        basicNewsService.deleteByADMIN(newsId);
        return ResponseEntity.ok("Success");
    }



}
