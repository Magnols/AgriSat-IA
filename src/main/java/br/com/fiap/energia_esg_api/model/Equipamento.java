package br.com.fiap.energia_esg_api.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EQUIPAMENTO")
@Getter
@Setter
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EQUIPAMENTO")
    private Long idEquipamento;

    @NotBlank(message = "O nome do equipamento e obrigatorio")
    @Size(max = 100, message = "O nome do equipamento deve ter no maximo 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O status do equipamento e obrigatorio")
    @Size(max = 20, message = "O status deve ter no maximo 20 caracteres")
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @NotNull(message = "O limite de consumo e obrigatorio")
    @Positive(message = "O limite de consumo deve ser maior que zero")
    @JdbcTypeCode(SqlTypes.NUMERIC)
    @Column(name = "LIMITE_CONSUMO", nullable = false, precision = 10, scale = 2)
    private Double limiteConsumo;

    @NotNull(message = "A area do equipamento e obrigatoria")
    @ManyToOne
    @JoinColumn(name = "ID_AREA", nullable = false)
    private Area area;
}
