package com.fiap.gs.controller;

import com.fiap.gs.dto.RotaSeguraDTO;
import com.fiap.gs.model.rotasegura.RotaSegura;
import com.fiap.gs.model.rotasegura.RotaSeguraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RotaSeguraControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RotaSeguraService rotaSeguraService;

    @InjectMocks
    private RotaSeguraController rotaSeguraController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(rotaSeguraController).build();
    }

    @Test
    public void testExibirFormCadastro() throws Exception {
        mockMvc.perform(get("/api/rotasegura/cadastrar"))
                .andExpect(status().isOk())  // Status 200 OK
                .andExpect(view().name("rotasegura/cadastrar"));
    }


    @Test
    public void testListarRotaSegura() throws Exception {
        List<RotaSegura> rotas = Arrays.asList(
                new RotaSegura(1L, "Rua X", "Bairro Y", "12345-678", "10", "Apto 101", "Cidade Z", "SP"),
                new RotaSegura(2L, "Rua A", "Bairro B", "23456-789", "20", "Apto 202", "Cidade W", "RJ")
        );

        when(rotaSeguraService.listarTodos()).thenReturn(rotas);

        mockMvc.perform(get("/api/rotasegura/listar"))
                .andExpect(status().isOk())  // Status 200 OK
                .andExpect(view().name("rotasegura/listar"))
                .andExpect(model().attribute("listaRotaSegura", rotas));
    }

    @Test
    public void testAtualizarRotaSegura() throws Exception {
        Long id = 1L;
        RotaSegura rotaSegura = new RotaSegura(id, "Rua X", "Bairro Y", "12345-678", "10", "Apto 101", "Cidade Z", "SP");
        RotaSeguraDTO dto = new RotaSeguraDTO(id, "Rua X", "Bairro Y", "12345-678", "10", "Apto 101", "Cidade Z", "SP");

        when(rotaSeguraService.buscarPorId(id)).thenReturn(rotaSegura);

        mockMvc.perform(get("/api/rotasegura/editar/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("rotasegura/editar"))
                .andExpect(model().attribute("RotaSeguraDTO", dto));
    }

    @Test
    public void testExcluirRotaSegura() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/api/rotasegura/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/rotasegura/listar"));

        verify(rotaSeguraService, times(1)).excluirEndereco(id);
    }
}