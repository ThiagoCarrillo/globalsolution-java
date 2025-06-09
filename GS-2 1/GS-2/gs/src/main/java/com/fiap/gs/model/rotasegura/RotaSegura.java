package com.fiap.gs.model.rotasegura;

import com.fiap.gs.dto.RotaSeguraDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rotasegura")
public class RotaSegura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logradouro;
    private String bairro;
    private String cep;
    private String numero;
    private String complemento;
    private String cidade;
    private String uf;

    public RotaSegura(RotaSeguraDTO rotaSeguraDTO) {
        this.id = rotaSeguraDTO.id();
        this.bairro = rotaSeguraDTO.bairro();
        this.logradouro = rotaSeguraDTO.logradouro();
        this.cep = rotaSeguraDTO.cep();
        this.numero = rotaSeguraDTO.numero();
        this.cidade = rotaSeguraDTO.cidade();
        this.complemento = rotaSeguraDTO.complemento();
        this.uf = rotaSeguraDTO.uf();
    }
}
