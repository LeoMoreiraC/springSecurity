package com.example.security.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
