package com.fiap.gs.model.IA;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiService {

    private final ChatClient chatClient;


    @Autowired
    public AiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String gerarResposta(String perguntaUsuario) {
        String instrucoesSistema = """
            Você é um assistente guarda civil especializado em rotas seguras.
            Forneça respostas em português brasileiro.
            Seja conciso e profissional.
           """;

        PromptTemplate template = new PromptTemplate("""
            {instrucoes}
            
            Pergunta do usuario:
            {pergunta}
            """);

        Prompt prompt = template.create(Map.of(
                "instrucoes", instrucoesSistema,
                "pergunta", perguntaUsuario
        ));

        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
}
