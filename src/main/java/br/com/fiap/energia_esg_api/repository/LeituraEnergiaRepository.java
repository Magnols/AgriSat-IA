package br.com.fiap.energia_esg_api.repository;

import br.com.fiap.energia_esg_api.model.LeituraEnergia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeituraEnergiaRepository extends JpaRepository<LeituraEnergia, Long> {
}