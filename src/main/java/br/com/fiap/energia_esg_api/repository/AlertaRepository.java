package br.com.fiap.energia_esg_api.repository;

import br.com.fiap.energia_esg_api.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
}