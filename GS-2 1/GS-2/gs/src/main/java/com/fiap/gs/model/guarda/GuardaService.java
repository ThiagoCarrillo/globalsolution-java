package com.fiap.gs.model.guarda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.gs.dto.GuardaCivilDTO;
import com.fiap.gs.model.rotasegura.RotaSegura;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GuardaService {

    private final GuardaRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public GuardaService(GuardaRepository repository, @Qualifier("rabbitTemplate") RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }


    public GuardaCivil cadastrar(GuardaCivilDTO dto) {
        GuardaCivil guardaCivil = new GuardaCivil(dto);
        enviarNotificacaoCadastro(guardaCivil);
        return repository.save(guardaCivil);
    }

    public GuardaCivil atualizar(GuardaCivilDTO dto) {
        if(dto.id() == null) {
            throw new IllegalArgumentException("Não encontrado nenhuma mensagem para Guarda Civil com esse id");
        }
        GuardaCivil guardaCivil = new GuardaCivil(dto);
        return repository.save(guardaCivil);
    }

    public GuardaCivil buscarPorId(Long id) {

        return repository.findById(id).
        orElseThrow(() -> new EntityNotFoundException("Nenhuma mensagem encontrada com esse id: " + id));
    }

    public List<GuardaCivil> listarTodos(){
        return repository.findAll();
    }


    public void excluir(Long id) {
        GuardaCivil guardaCivil = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma mensagem encontrada com esse id: " + id));
        repository.delete(guardaCivil);
    }

    private void enviarNotificacaoCadastro(GuardaCivil guardaCivil) {
            Map<String, Object> mensagem = new HashMap<>();
            mensagem.put("tipo", "cadastro_guarda_civil");
            mensagem.put("id", guardaCivil.getId());
            mensagem.put("mensagem", guardaCivil.getMensagem());
            mensagem.put("email", guardaCivil.getEmail());
            mensagem.put("dataCadastro", LocalDateTime.now().toString());

            rabbitTemplate.convertAndSend(
                    "cadastro.exchange",
                    "cadastro.guardacivil",
                    mensagem
            );
    }
}
