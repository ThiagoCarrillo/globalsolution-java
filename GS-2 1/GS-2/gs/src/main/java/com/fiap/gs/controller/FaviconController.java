package com.fiap.gs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaviconController {

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        // Retorna 204 No Content para não gerar erro 404 e não enviar nada
        return ResponseEntity.noContent().build();
    }
}

