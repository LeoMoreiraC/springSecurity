package com.example.security.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationResponse {
    private String accessToken;
}
