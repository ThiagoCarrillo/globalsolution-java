package com.fiap.gs.model.rotasegura;

import com.fiap.gs.dto.RotaSeguraDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RotaSeguraServiceTest {

    @Mock
    private RotaSeguraRepository rotaSeguraRepository;

    @InjectMocks
    private RotaSeguraService rotaSeguraService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testCriarRotaSegura() {

        RotaSeguraDTO dto = new RotaSeguraDTO(null, "Rua X", "Bairro Y", "12345-678", "10", "Apto 101", "Cidade Z", "SP");
        RotaSegura rotaSegura = new RotaSegura(dto);

        when(rotaSeguraRepository.save(any(RotaSegura.class))).thenReturn(rotaSegura);

        RotaSegura result = rotaSeguraService.criarRotaSegura(dto);

        assertNotNull(result);
        assertEquals("Rua X", result.getLogradouro());
        verify(rotaSeguraRepository, times(1)).save(any(RotaSegura.class));
    }

    @Test
    public void testAtualizarRotaSegura() {
        RotaSeguraDTO dto = new RotaSeguraDTO(1L, "Rua X", "Bairro Y", "12345-678", "10", "Apto 101", "Cidade Z", "SP");
        RotaSegura rotaSegura = new RotaSegura(dto);

        when(rotaSeguraRepository.save(any(RotaSegura.class))).thenReturn(rotaSegura);

        RotaSegura result = rotaSeguraService.atualizarRotaSegura(dto);

        assertNotNull(result);
        assertEquals("Rua X", result.getLogradouro());
        verify(rotaSeguraRepository, times(1)).save(any(RotaSegura.class));
    }

    @Test
    public void testBuscarPorId() {
        RotaSegura rotaSegura = new RotaSegura(1L, "Rua X", "Bairro Y", "12345-678", "10", "Apto 101", "Cidade Z", "SP");

        when(rotaSeguraRepository.findById(1L)).thenReturn(Optional.of(rotaSegura));

        RotaSegura result = rotaSeguraService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals("Rua X", result.getLogradouro());
        verify(rotaSeguraRepository, times(1)).findById(1L);
    }

    @Test
    public void testBuscarPorId_Exception() {
        when(rotaSeguraRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rotaSeguraService.buscarPorId(1L));
    }

    @Test
    public void testListarTodos() {
        List<RotaSegura> rotas = Arrays.asList(
                new RotaSegura(1L, "Rua X", "Bairro Y", "12345-678", "10", "Apto 101", "Cidade Z", "SP"),
                new RotaSegura(2L, "Rua A", "Bairro B", "23456-789", "20", "Apto 202", "Cidade W", "RJ")
        );

        when(rotaSeguraRepository.findAll()).thenReturn(rotas);

        List<RotaSegura> result = rotaSeguraService.listarTodos();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(rotaSeguraRepository, times(1)).findAll();
    }

    @Test
    public void testExcluirEndereco() {
        Long id = 1L;
        RotaSegura rotaSegura = new RotaSegura(id, "Rua X", "Bairro Y", "12345-678", "10", "Apto 101", "Cidade Z", "SP");

        when(rotaSeguraRepository.findById(id)).thenReturn(Optional.of(rotaSegura));

        rotaSeguraService.excluirEndereco(id);

        verify(rotaSeguraRepository, times(1)).delete(rotaSegura);
    }

    @Test
    public void testExcluirEndereco_Exception() {
        Long id = 1L;
        when(rotaSeguraRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rotaSeguraService.excluirEndereco(id));
    }
}
