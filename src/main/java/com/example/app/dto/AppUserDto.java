package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String avatar;
}
