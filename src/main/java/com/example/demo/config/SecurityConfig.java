package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    private final SuccessUserHandler successUserHandler;

    public SecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/admin/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET,  "/user/**" )
                .hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
            )
                .formLogin(form -> form
                        .successHandler(successUserHandler)
                        .permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
