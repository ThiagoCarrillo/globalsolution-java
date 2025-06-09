package com.fiap.gs.controller;

import com.fiap.gs.dto.GuardaCivilDTO;
import com.fiap.gs.model.guarda.GuardaCivil;
import com.fiap.gs.model.guarda.GuardaService;
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
public class GuardaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GuardaService guardaService;

    @InjectMocks
    private GuardaController guardaController;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(guardaController).build();
    }

    @Test
    public void testExibirFormularioCadastro() throws Exception {
        mockMvc.perform(get("/api/guarda/cadastrar"))
                .andExpect(status().isOk())
                .andExpect(view().name("guardaCivil/cadastrar"));
    }


    @Test
    public void testListarMensagens() throws Exception {
        List<GuardaCivil> guardas = Arrays.asList(
                new GuardaCivil(1L, "Mensagem 1", "email1@teste.com"),
                new GuardaCivil(2L, "Mensagem 2", "email2@teste.com")
        );

        when(guardaService.listarTodos()).thenReturn(guardas);

        mockMvc.perform(get("/api/guarda/listar"))
                .andExpect(status().isOk())
                .andExpect(view().name("guardaCivil/listar"))
                .andExpect(model().attribute("listaGuardaCivil", guardas));
    }

    @Test
    public void testAtualizarGuardaCivil() throws Exception {
        Long id = 1L;
        GuardaCivil guarda = new GuardaCivil(id, "Mensagem de teste", "email@teste.com");
        GuardaCivilDTO dto = new GuardaCivilDTO(id, "Mensagem de teste", "email@teste.com");

        when(guardaService.buscarPorId(id)).thenReturn(guarda);

        mockMvc.perform(get("/api/guarda/{id}/editar", id))
                .andExpect(status().isOk())
                .andExpect(view().name("guardaCivil/editar"))
                .andExpect(model().attribute("GuardaCivilDTO", dto));
    }

    @Test
    public void testExcluirMensagem() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/api/guarda/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/api/guarda/listar"));

        verify(guardaService, times(1)).excluir(id);
    }
}