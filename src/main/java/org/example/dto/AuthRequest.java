package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthRequest {

    @Schema(description = "기본 아이디", example = "test@test.com")
    private String email;

    @Schema(description = "기본 비번", example = "1234")
    private String password;
}