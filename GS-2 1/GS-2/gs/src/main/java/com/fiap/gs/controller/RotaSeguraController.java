package com.fiap.gs.controller;

import com.fiap.gs.dto.RotaSeguraDTO;
import com.fiap.gs.model.rotasegura.RotaSegura;
import com.fiap.gs.model.rotasegura.RotaSeguraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/rotasegura")
public class RotaSeguraController {

    @Autowired
    private RotaSeguraService rotaSeguraService;


    @GetMapping("/cadastrar")
    public String exibirFormCadastro(Model model) {
        model.addAttribute("RotaSegura", new RotaSegura());
        return "rotasegura/cadastrar";
    }

    @PostMapping("/cadastrar")
    public String cadastrarRotaSegura(@ModelAttribute("RotaSeguraDTO")RotaSeguraDTO rotaSeguraDTO)
    {
        if(rotaSeguraDTO.id() == null) {
            rotaSeguraService.criarRotaSegura(rotaSeguraDTO);
        }else{
            rotaSeguraService.atualizarRotaSegura(rotaSeguraDTO);
        }
        return "redirect:/api/rotasegura/listar";
    }

    @GetMapping("/listar")
    public String listaRotaSegura(Model model) {
        List<RotaSegura> page = rotaSeguraService.listarTodos();
        model.addAttribute("listaRotaSegura", page);
        return "rotasegura/listar";
    }


    @GetMapping("/editar/{id}")
    public String atualizarRotaSegura(
            @PathVariable Long id,
            Model model
    ) {
        RotaSegura rotaSegura = rotaSeguraService.buscarPorId(id);
        RotaSeguraDTO dto = new RotaSeguraDTO(
                rotaSegura.getId(),
                rotaSegura.getLogradouro(),
                rotaSegura.getBairro(),
                rotaSegura.getCep(),
                rotaSegura.getNumero(),
                rotaSegura.getComplemento(),
                rotaSegura.getCidade(),
                rotaSegura.getUf()
        );
        model.addAttribute("RotaSeguraDTO", dto);

        return "rotasegura/editar";
    }


    @GetMapping("/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        rotaSeguraService.excluirEndereco(id);
        return "redirect:/api/rotasegura/listar";
    }
}
