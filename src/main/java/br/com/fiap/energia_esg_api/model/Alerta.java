package br.com.fiap.energia_esg_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ALERTA")
@Getter
@Setter
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALERTA")
    private Long idAlerta;

    @NotBlank(message = "A descricao do alerta e obrigatoria")
    @Size(max = 200, message = "A descricao deve ter no maximo 200 caracteres")
    @Column(name = "DESCRICAO", nullable = false, length = 200)
    private String descricao;

    @NotNull(message = "A data e hora do alerta e obrigatoria")
    @Column(name = "DATA_HORA", nullable = false)
    private LocalDateTime dataHora;

    @NotNull(message = "O equipamento do alerta e obrigatorio")
    @ManyToOne
    @JoinColumn(name = "ID_EQUIPAMENTO", nullable = false)
    private Equipamento equipamento;
}
