package com.fiap.gs.dto;

import lombok.Getter;
import lombok.Setter;

public record RotaSeguraDTO(Long id, String logradouro, String bairro, String cep, String numero, String complemento, String cidade, String uf) {
}
