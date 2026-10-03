package br.com.fiap.energia_esg_api.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "CONSUMO_DIARIO")
@Getter
@Setter
public class ConsumoDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONSUMO")
    private Long idConsumo;

    @NotNull(message = "A data de referencia e obrigatoria")
    @Column(name = "DATA_REFERENCIA", nullable = false)
    private LocalDate dataReferencia;

    @NotNull(message = "O consumo total e obrigatorio")
    @PositiveOrZero(message = "O consumo total nao pode ser negativo")
    @JdbcTypeCode(SqlTypes.NUMERIC)
    @Column(name = "TOTAL_CONSUMO", nullable = false, precision = 10, scale = 2)
    private Double totalConsumo;

    @NotNull(message = "O equipamento e obrigatorio")
    @ManyToOne
    @JoinColumn(name = "ID_EQUIPAMENTO", nullable = false)
    private Equipamento equipamento;
}
