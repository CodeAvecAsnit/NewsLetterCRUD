package com.news.lettercrud.Controller;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Security.UserDetailsImpl;
import com.news.lettercrud.Services.news.UpdateNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class UpdateNewsController {

    private final UpdateNewsService updateNewsService;


    @Autowired
    public UpdateNewsController(@Qualifier("updateNewsServiceImpl") UpdateNewsService updateNewsService) {
        this.updateNewsService = updateNewsService;
    }

    @Operation(security =  @SecurityRequirement(name="bearerAuth"))
    @PutMapping("/update")
    public ResponseEntity<?> updateNews(@RequestParam int newsId,
                                        @RequestBody CreateORUpdateNewsDTO data,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        updateNewsService.updateNews(newsId,data, userDetails.getId());
        return ResponseEntity.ok("Success");
    }

    @Operation(security =  @SecurityRequirement(name="bearerAuth"))
    @PutMapping("/admin/update")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> updateNewsByAdmin(@RequestParam int newsId,
                                        @RequestBody CreateORUpdateNewsDTO data,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        updateNewsService.updateNewsByAdmin(newsId,data);
        return ResponseEntity.ok("Success");
    }
}
