package com.fiap.gs.infra.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfiguration(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                // Configurar autorizações
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/",
                                "/login",
                                "/oauth2/**",
                                "/webjars/**",
                                "/home",
                                "/initial",
                                "/api/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/api/guarda/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // Configurar OAuth2 Login
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/initial")
                        .defaultSuccessUrl("/home", true)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )

                // Configurar logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/initial?logout")
                        .permitAll()
                )

                // Configurar sessão (opcional, mas recomendado)
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                );

        return http.build();
    }
}
