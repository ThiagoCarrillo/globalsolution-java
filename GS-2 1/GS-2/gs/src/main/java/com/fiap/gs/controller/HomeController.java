package com.fiap.gs.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    // GET "/" → redireciona para /home
    @GetMapping("/")
    public String root(@AuthenticationPrincipal OAuth2User principal, Model model) {

        model.addAttribute("titulo", "Bem-vindo ao Sistema");

        if (principal != null) {
            String nome  = principal.getAttribute("name");
            String email = principal.getAttribute("email");
            model.addAttribute("nome", nome);
            model.addAttribute("email", email);
        }
        return "initial";
    }

    // GET "/home" → exibe nome/email (se estiver logado) ou botão de login
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
