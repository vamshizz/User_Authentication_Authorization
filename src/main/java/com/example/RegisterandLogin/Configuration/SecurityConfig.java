package com.example.RegisterandLogin.Configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private  JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          http.
                csrf(csrf->csrf.disable())
                        .authorizeHttpRequests(auth->
                                auth.
                                        requestMatchers("/signup","/login").permitAll()
                                        .anyRequest().authenticated()
                        )
                  .httpBasic(Customizer.withDefaults())
                  .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

               return http.build();
    }

}
