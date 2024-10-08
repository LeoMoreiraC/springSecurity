package com.example.security.config.converter;

import com.example.security.domain.model.User;
import com.example.security.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDTOConverter {
    public UserDTO fromUser(User user){
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .build();
    }
}
