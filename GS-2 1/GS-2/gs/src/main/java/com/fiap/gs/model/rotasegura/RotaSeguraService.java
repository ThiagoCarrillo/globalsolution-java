package com.fiap.gs.model.rotasegura;

import com.fiap.gs.dto.RotaSeguraDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RotaSeguraService {

    @Autowired
    private RotaSeguraRepository rotaSeguraRepository;

    public RotaSegura criarRotaSegura(RotaSeguraDTO rotaSeguraDTO) {
        RotaSegura rotaSegura = new RotaSegura(rotaSeguraDTO);
        return rotaSeguraRepository.save(rotaSegura);
    }

    public RotaSegura atualizarRotaSegura(RotaSeguraDTO rotaSeguraDTO) {
        if (rotaSeguraDTO.id() == null){
            throw new IllegalArgumentException("Médico não encontrado para atualização");
        }
        RotaSegura rotaSegura = new RotaSegura(rotaSeguraDTO);
        return rotaSeguraRepository.save(rotaSegura);
    }

    public RotaSegura buscarPorId(Long id) {
        return rotaSeguraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma rota segura encotrado: " + id));
    }

    public List<RotaSegura> listarTodos() {
        return rotaSeguraRepository.findAll();
    }

    public void excluirEndereco(Long id) {
        RotaSegura endereco = rotaSeguraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id: " + id));
        rotaSeguraRepository.delete(endereco);
    }
}
