package com.news.lettercrud.controller;

import com.news.lettercrud.data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.security.UserDetailsImpl;
import com.news.lettercrud.service.news.AddNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Asnit Bakhati
 */

@RestController
@RequestMapping("/api/v1/add")
public class AddNewsController {
    private final AddNewsService addNewsService;

    @Autowired
    public AddNewsController(@Qualifier("addNewsServiceImpl")@Valid AddNewsService addNewsService) {
        this.addNewsService = addNewsService;
    }


    @Operation(summary = "Post News accessible only by COMPANY or ADMIN",
    security = @SecurityRequirement(name="bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','SUPER_ADMIN')")
    @PostMapping("/news")
    public ResponseEntity<?> postNews(@RequestBody CreateORUpdateNewsDTO data,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(addNewsService.postNews(data,userDetails.getId()));
    }

    @Operation(summary = "Filter news by News Category")
    @GetMapping("/find")
    public ResponseEntity<?> getNewsCat(@RequestParam String category){
        return ResponseEntity.ok(addNewsService.findNewsWithCategory(category));
    }
}
