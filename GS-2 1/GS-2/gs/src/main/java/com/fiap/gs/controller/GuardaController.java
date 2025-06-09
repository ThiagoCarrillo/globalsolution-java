package com.fiap.gs.controller;

import com.fiap.gs.dto.GuardaCivilDTO;
import com.fiap.gs.model.guarda.GuardaCivil;
import com.fiap.gs.model.guarda.GuardaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/guarda")
public class GuardaController {

    private final GuardaService guardaService;

    @Autowired
    public GuardaController(GuardaService guardaService) {
        this.guardaService = guardaService;
    }

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("Guarda", new GuardaCivil());
        return "guardaCivil/cadastrar";
    }

    @PostMapping("/cadastrar")
    @Transactional
    public String cadastrarMensagem(
            @Valid @ModelAttribute("mensagemForm") GuardaCivilDTO dados) {

        if(dados.id() == null) {
            guardaService.cadastrar(dados);
        }else {
            guardaService.atualizar(dados);
        }

        return "redirect:/api/guarda/listar";
    }

    @GetMapping("/listar")
    public String listarMensagensView(Model model) {
        List<GuardaCivil> page = guardaService.listarTodos();
        model.addAttribute("listaGuardaCivil", page);
        return "guardaCivil/listar";
    }


    @GetMapping("/{id}/editar")
    public String atualizarGuardaCivil(
            @PathVariable Long id,
            Model model
    ) {
        GuardaCivil guardaCivil = guardaService.buscarPorId(id);
        GuardaCivilDTO dto = new GuardaCivilDTO(
                guardaCivil.getId(),
                guardaCivil.getMensagem(),
                guardaCivil.getEmail()
        );
        model.addAttribute("GuardaCivilDTO", dto);
        return "guardaCivil/editar";
    }


    @GetMapping("/{id}")
    @Transactional
    public String excluirMensagem(@PathVariable Long id) {
        guardaService.excluir(id);
        return "redirect:/api/guarda/listar";
    }
}
