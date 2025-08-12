package com.Group6.WebAppDevGroupProject;

import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    public UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> usersDetails_ = new ArrayList<>();
        List<User> user_ = userService.findAll();

        for (User usr_ : user_) {
            usersDetails_.add(org.springframework.security.core.userdetails.User.builder()
                    .username(usr_.getUsername())
                    .password(passwordEncoder().encode(usr_.getPassword()))
                    .roles(usr_.getRole().toUpperCase())
                    .build());
        }
        return new InMemoryUserDetailsManager(usersDetails_);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/login", "/users/register", "/css/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/users/login")
                .defaultSuccessUrl("/orders/active")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/users/login")
                .logoutSuccessUrl("/users/login")
        );
        return http.build();
    }
}