package com.fiap.consumer_gs.consumer;

import com.fiap.consumer_gs.EmailService.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class CadastroGuardaCivilConsumer {

    private final EmailService emailService;

    public CadastroGuardaCivilConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${rabbitmq.guarda-civil.queue}")
    public void processarCadastro(Map<String, Object> mensagem) {
        try {
            log.info("Mensagem recebida: {}", mensagem);

            String email = (String) mensagem.get("email");
            String assunto = "Confirmação de Cadastro - Guarda Civil";
            String corpo = "Olá,\n\n" +
                    "Seu cadastro na Guarda Civil foi realizado com sucesso!\n\n" +
                    "Detalhes:\n" +
                    "Mensagem: " + mensagem.get("mensagem") + "\n" +
                    "Data: " + mensagem.get("dataCadastro") + "\n\n" +
                    "Atenciosamente,\nEquipe Guarda Civil";

            emailService.enviarEmail(email, assunto, corpo);
            log.info("Notificação enviada para: {}", email);

        } catch (Exception e) {
            log.error("ERRO AO PROCESSAR CADASTRO: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = "${rabbitmq.guarda-civil.dlq}")
    public void processarDLQ(Map<String, Object> mensagemFalha) {
        log.error("Mensagem na DLQ: {}", mensagemFalha);
    }
}
