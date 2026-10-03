package br.com.fiap.energia_esg_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LeituraEnergiaRequestDTO {

    @NotNull(message = "A data e hora da leitura e obrigatoria")
    private LocalDateTime dataHora;

    @NotNull(message = "O consumo e obrigatorio")
    @Positive(message = "O consumo deve ser maior que zero")
    private Double consumo;

    @NotNull(message = "O id do equipamento e obrigatorio")
    private Long idEquipamento;
}
