package br.com.fiap.energia_esg_api.service;

import br.com.fiap.energia_esg_api.model.Alerta;
import br.com.fiap.energia_esg_api.repository.AlertaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;

    public AlertaService(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public List<Alerta> listar() {
        return alertaRepository.findAll();
    }
}
