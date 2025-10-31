package com.news.lettercrud.data.DTOs;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema (description = "Sending mail with verification")
public class MailVerificationDTO {

    private String email;

    private int code;

}
