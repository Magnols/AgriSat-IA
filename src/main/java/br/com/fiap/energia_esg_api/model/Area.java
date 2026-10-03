package br.com.fiap.energia_esg_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "AREA")
@Getter
@Setter
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AREA")
    private Long idArea;

    @NotBlank(message = "O nome da area e obrigatorio")
    @Size(max = 100, message = "O nome da area deve ter no maximo 100 caracteres")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Size(max = 200, message = "A descricao deve ter no maximo 200 caracteres")
    @Column(name = "DESCRICAO", length = 200)
    private String descricao;
}
