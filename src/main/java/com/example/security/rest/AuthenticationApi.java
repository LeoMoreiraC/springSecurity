package com.example.security.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.domain.model.User;
import com.example.security.dto.AuthenticationRequest;
import com.example.security.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequiredArgsConstructor
@RestController
public class AuthenticationApi {
    private final AuthenticationManager authenticationManager;
    @Value("${desafio.auth.password}")
    private String tokenPassword;
    @Value("${desafio.auth.expirationTime}")
    private Long tokenExpire;

    //faz login na aplicacao, retornando o token
    @Transactional(readOnly = true)
    @PostMapping(value = "/api/v1/oauth/token", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthenticationResponse> autenticar(@RequestBody AuthenticationRequest oauthRequest){
        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(oauthRequest.getUsername(),
                oauthRequest.getPassword()));
        var user = (User) authentication.getPrincipal();
        var token = JWT.create()
                .withClaim("id", user.getId())
                .withSubject(oauthRequest.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+tokenExpire*1000))
                .sign(Algorithm.HMAC256(tokenPassword));
        var response = AuthenticationResponse.builder().accessToken(token).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
