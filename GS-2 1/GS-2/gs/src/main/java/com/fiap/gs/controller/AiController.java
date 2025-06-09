package com.fiap.gs.controller;

import com.fiap.gs.model.IA.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AiController {
    private final AiService aiService;

    @Autowired
    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/ai-chat")
    public String mostrarFormularioChat() {
        return "chat/formulario";
    }

    @PostMapping("/ai-chat")
    public String processarPergunta(
            @RequestParam String pergunta,
            Model model
    ) {
        String resposta = aiService.gerarResposta(pergunta);
        model.addAttribute("pergunta", pergunta);
        model.addAttribute("resposta", resposta);
        return "chat/resposta";
    }
}
