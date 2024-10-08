package com.example.security.config.exception.handler;

import com.example.security.config.exception.AppException;
import com.example.security.dto.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ControllerAdvice
public class ExceptionHandlerConfig {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDTO> appException(AppException appException){
        var errorDto = new ErrorDTO(appException.getTitle(),
                appException.getDescription());
        return ResponseEntity.status(appException.getStatusCode())
                .body(errorDto);
    }
}
