package br.com.fiap.energia_esg_api.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "LEITURA_ENERGIA")
@Getter
@Setter
public class LeituraEnergia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LEITURA")
    private Long idLeitura;

    @NotNull(message = "A data e hora da leitura e obrigatoria")
    @Column(name = "DATA_HORA", nullable = false)
    private LocalDateTime dataHora;

    @NotNull(message = "O consumo e obrigatorio")
    @Positive(message = "O consumo deve ser maior que zero")
    @JdbcTypeCode(SqlTypes.NUMERIC)
    @Column(name = "CONSUMO", nullable = false, precision = 10, scale = 2)
    private Double consumo;

    @NotNull(message = "O equipamento da leitura e obrigatorio")
    @ManyToOne
    @JoinColumn(name = "ID_EQUIPAMENTO", nullable = false)
    private Equipamento equipamento;
}
