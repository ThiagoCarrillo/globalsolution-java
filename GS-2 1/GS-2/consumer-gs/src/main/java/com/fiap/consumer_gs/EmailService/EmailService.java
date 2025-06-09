package com.fiap.consumer_gs.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmail(String destinatario, String assunto, String corpo) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(remetente);
            mensagem.setTo(destinatario);
            mensagem.setSubject(assunto);
            mensagem.setText(corpo);

            mailSender.send(mensagem);
            log.info("E-mail enviado para: {}", destinatario);

        } catch (Exception e) {
            log.error("FALHA AO ENVIAR E-MAIL PARA {}: {}", destinatario, e.getMessage());
            log.debug("Detalhes do erro:", e);
            throw new EmailException("Falha no envio de e-mail", e);
        }
    }

    public static class EmailException extends RuntimeException {
        public EmailException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}