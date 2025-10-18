package com.news.lettercrud.Controller;

import com.news.lettercrud.Data.DTOs.CreateORUpdateNewsDTO;
import com.news.lettercrud.Security.UserDetailsImpl;
import com.news.lettercrud.Services.news.AddNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Asnit Bakhati
 */

@RestController
@RequestMapping("/api/v1/add")
@PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','SUPER_ADMIN')")
public class AddNewsController {
    private final AddNewsService addNewsService;

    @Autowired
    public AddNewsController(@Qualifier("addNewsServiceImpl") AddNewsService addNewsService) {
        this.addNewsService = addNewsService;
    }


    @Operation(summary = "Post News accessible only by COMPANY or ADMIN",
    security = @SecurityRequirement(name="bearerAuth"))
    @PostMapping("/news")
    public ResponseEntity<?> postNews(@RequestBody CreateORUpdateNewsDTO data,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(addNewsService.postNews(data,userDetails.getId()));
    }
}
