package com.news.lettercrud.Controller;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Security.UserDetailsImpl;
import com.news.lettercrud.Services.news.UpdateNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class UpdateNewsController {

    private final UpdateNewsService updateNewsService;


    @Autowired
    public UpdateNewsController(@Qualifier("updateNewsServiceImpl") UpdateNewsService updateNewsService) {
        this.updateNewsService = updateNewsService;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateNews(@RequestParam int newsId,
                                        @RequestBody CreateORUpdateNewsDTO data,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        updateNewsService.updateNews(newsId,data, userDetails.getId());
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/admin/update")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> updateNewsByAdmin(@RequestParam int newsId,
                                        @RequestBody CreateORUpdateNewsDTO data,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        updateNewsService.updateNewsByAdmin(newsId,data);
        return ResponseEntity.ok("Success");
    }
}
