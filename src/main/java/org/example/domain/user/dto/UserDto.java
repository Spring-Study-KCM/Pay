package org.example.domain.user.dto;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String password;
}