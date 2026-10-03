package br.com.fiap.energia_esg_api.service;

import br.com.fiap.energia_esg_api.model.ConsumoDiario;
import br.com.fiap.energia_esg_api.repository.ConsumoDiarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumoDiarioService {

    private final ConsumoDiarioRepository consumoDiarioRepository;

    public ConsumoDiarioService(ConsumoDiarioRepository consumoDiarioRepository) {
        this.consumoDiarioRepository = consumoDiarioRepository;
    }

    public List<ConsumoDiario> listarConsumoDiario() {
        return consumoDiarioRepository.findAll();
    }
}
