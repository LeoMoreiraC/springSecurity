package com.example.security.config.security;

import com.example.security.config.security.JWT.JWTAuthenticationFilter;
import com.example.security.config.security.JWT.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUserDetailsService JWTUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/oauth/token").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement(cust -> cust.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // filtra requisições de login
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JWTAuthenticationFilter authenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(JWTUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
