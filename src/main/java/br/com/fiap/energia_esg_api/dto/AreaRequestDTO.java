package br.com.fiap.energia_esg_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AreaRequestDTO {

    @NotBlank(message = "O nome da area e obrigatorio")
    @Size(max = 100, message = "O nome da area deve ter no maximo 100 caracteres")
    private String nome;

    @Size(max = 200, message = "A descricao deve ter no maximo 200 caracteres")
    private String descricao;
}
