package br.com.fiap.energia_esg_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipamentoRequestDTO {

    @NotBlank(message = "O nome do equipamento e obrigatorio")
    @Size(max = 100, message = "O nome do equipamento deve ter no maximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O status do equipamento e obrigatorio")
    @Size(max = 20, message = "O status deve ter no maximo 20 caracteres")
    private String status;

    @NotNull(message = "O limite de consumo e obrigatorio")
    @Positive(message = "O limite de consumo deve ser maior que zero")
    private Double limiteConsumo;

    @NotNull(message = "O id da area e obrigatorio")
    private Long idArea;
}
