package com.fiap.gs.model.guarda;

import com.fiap.gs.dto.GuardaCivilDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import jakarta.persistence.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GuardaServiceTest {

    @Mock
    private GuardaRepository guardaRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private GuardaService guardaService;
    @BeforeEach
    public void setup() {
    }

    @Test
    public void testCadastrar() {
        // Dado
        GuardaCivilDTO dto = new GuardaCivilDTO(null, "Mensagem de teste", "email@teste.com");
        GuardaCivil guardaCivil = new GuardaCivil(dto);

        when(guardaRepository.save(any(GuardaCivil.class))).thenReturn(guardaCivil);

        // Quando
        GuardaCivil result = guardaService.cadastrar(dto);

        // Então
        assertNotNull(result);
        assertEquals("Mensagem de teste", result.getMensagem());
        verify(guardaRepository, times(1)).save(any(GuardaCivil.class));
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyMap());
    }

    @Test
    public void testAtualizar() {
        GuardaCivilDTO dto = new GuardaCivilDTO(1L, "Mensagem de teste atualizada", "email@teste.com");
        GuardaCivil guardaCivil = new GuardaCivil(dto);

        when(guardaRepository.save(any(GuardaCivil.class))).thenReturn(guardaCivil);

        GuardaCivil result = guardaService.atualizar(dto);


        assertNotNull(result);
        assertEquals("Mensagem de teste atualizada", result.getMensagem());
        verify(guardaRepository, times(1)).save(any(GuardaCivil.class));
    }

    @Test
    public void testBuscarPorId() {
        GuardaCivil guardaCivil = new GuardaCivil(1L, "Mensagem de teste", "email@teste.com");

        when(guardaRepository.findById(1L)).thenReturn(Optional.of(guardaCivil));

        GuardaCivil result = guardaService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals("Mensagem de teste", result.getMensagem());
        verify(guardaRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorId_Exception() {
        when(guardaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> guardaService.buscarPorId(1L));
    }

    @Test
    public void testListarTodos() {
        List<GuardaCivil> guardas = Arrays.asList(
                new GuardaCivil(1L, "Mensagem 1", "email1@teste.com"),
                new GuardaCivil(2L, "Mensagem 2", "email2@teste.com")
        );

        when(guardaRepository.findAll()).thenReturn(guardas);

        List<GuardaCivil> result = guardaService.listarTodos();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(guardaRepository, times(1)).findAll();
    }

    @Test
    public void testExcluir() {
        Long id = 1L;
        GuardaCivil guardaCivil = new GuardaCivil(id, "Mensagem para excluir", "email@teste.com");

        when(guardaRepository.findById(id)).thenReturn(Optional.of(guardaCivil));

        guardaService.excluir(id);

        verify(guardaRepository, times(1)).delete(guardaCivil);
    }

    @Test
    public void testExcluir_Exception() {
        Long id = 1L;
        when(guardaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> guardaService.excluir(id));
    }
}
