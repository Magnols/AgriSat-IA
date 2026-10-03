package br.com.fiap.energia_esg_api.repository;

import br.com.fiap.energia_esg_api.model.ConsumoDiario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumoDiarioRepository extends JpaRepository<ConsumoDiario, Long> {
}