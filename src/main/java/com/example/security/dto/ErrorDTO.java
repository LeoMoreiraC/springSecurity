package com.example.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ErrorDTO {
    private final String title;
    private final String description;
}
