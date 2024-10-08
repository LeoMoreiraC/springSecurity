package com.example.security.config.security.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.security.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${desafio.auth.password}")
    private String tokenPassword;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        log.info("[{}] '{}' from '{}'", request.getMethod(),
                request.getRequestURI(), request.getRemoteAddr());
        var token = validarToken(request);
        if (token.isPresent()) {
            var authentication = createToken(token.get());
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private Optional<DecodedJWT> validarToken(HttpServletRequest request) {
        var token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith(BEARER_PREFIX))
            return Optional.empty();
        var decodedToken = JWT.require(Algorithm.HMAC256(tokenPassword)).build()
                .verify(token.replace(BEARER_PREFIX, ""));
        return Optional.ofNullable(decodedToken);
    }

    private UsernamePasswordAuthenticationToken createToken(DecodedJWT value) {
        var user = User.builder()
                .id(value.getClaim("id").asLong())
                .login(value.getSubject())
                .build();
        return new UsernamePasswordAuthenticationToken(user,null, new ArrayList<>());
    }
}
