package com.fiap.gs.model.guarda;

import com.fiap.gs.dto.GuardaCivilDTO;
import com.fiap.gs.dto.RotaSeguraDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "guarda")
@EqualsAndHashCode(of = "id")
public class GuardaCivil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    private String mensagem;

    @NotBlank
    private String email;


    public GuardaCivil(GuardaCivilDTO guardaCivilDTO) {
        this.id = guardaCivilDTO.id();
        this.mensagem = guardaCivilDTO.mensagem();
        this.email = guardaCivilDTO.email();
    }
}
