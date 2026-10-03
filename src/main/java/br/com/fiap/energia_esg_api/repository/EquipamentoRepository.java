package br.com.fiap.energia_esg_api.repository;

import br.com.fiap.energia_esg_api.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
}