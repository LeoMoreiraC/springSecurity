package com.example.security.rest;

import com.example.security.dto.UserDTO;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4000", maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    //cria usuario
    @PostMapping(value = "/api/v1/user", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> save(@RequestBody UserDTO userDTO){
        if(userDTO.getId() != null)
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userService.salvarUser(userDTO));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.salvarUser(userDTO));

    }

    //busca todos usuarios
    @GetMapping(value = "/api/v1/user", produces = "application/json")
    public ResponseEntity<List<UserDTO>> findAll(Pageable pageable){
        return ResponseEntity.ok(userService.buscarTodos(pageable));
    }

    //busca usuarios por ID
    @GetMapping(value = "/api/v1/user/{id}", produces = "application/json")
    public ResponseEntity<?> findById(@PathVariable Long id){
        var userResponse = userService.buscarById(id);
        if(userResponse == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(userResponse);
    }
}
